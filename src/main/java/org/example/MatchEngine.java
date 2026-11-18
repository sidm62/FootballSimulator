package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchEngine implements Runnable {

    private final Match match;
    private final GameController controller;
    private final Random rand = new Random();

    private volatile boolean isRunning = true;
    private volatile boolean isPaused = false;

    private int currentMinute = 0;
    private int tickSpeedMs = 400; // 400 ms per peliminuutti

    public MatchEngine(Match match, GameController controller) {
        this.match = match;
        this.controller = controller;
    }

    @Override
    public void run() {
        match.prepareMatch();

        // 1. PUOLIAIKA
        simulateHalf(1, 45);

        triggerAssistantCoachHalfTime();
        processQueueSubstitutions(45, EventType.HALF_TIME);
        waitIfPaused();

        // 2. PUOLIAIKA
        if (isRunning) {
            simulateHalf(46, 90);
            MatchEvent fullTimeEvent = new MatchEvent(90, "Ottelu päättyi, tuomari viheltää pilliin!", EventType.FULL_TIME, null);
            controller.onEventLogged(fullTimeEvent);
            match.logEvent(fullTimeEvent);

            match.printMatchSummary();
        }
    }

    private void simulateHalf(int startMin, int endMin) {
        for (int m = startMin; m <= endMin; m++) {
            if (!isRunning) break;

            waitIfPaused();
            this.currentMinute = m;

            controller.onMinuteTick(currentMinute, match.getHomeScore(), match.getAwayScore());
            simulateMinuteLogic(currentMinute);
            checkAssistantCoachTriggers(currentMinute);

            try {
                Thread.sleep(tickSpeedMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    private void simulateMinuteLogic(int minute) {
        Team home = match.getHomeTeam();
        Team away = match.getAwayTeam();
        Referee referee = match.getReferee();

        // 1. STAMINA-KULUTUS (Huomioi ulosajot ja taktiikan tempon)
        drainStaminaForTeam(home);
        drainStaminaForTeam(away);

        // 2. TEMPON VAIKUTUS SYÖTTÖVOLYYMIIN
        // Haetaan temppomäärä taktiikoista (asteikko 1-10 -> keskiarvo)
        int homeTempo = (home.getTactic() != null) ? home.getTactic().getTempo() : 5;
        int awayTempo = (away.getTactic() != null) ? away.getTactic().getTempo() : 5;
        double avgTempo = (homeTempo + awayTempo) / 2.0;

        // Tempo mittaa pelin nopeutta: korkeampi tempo = enemmän syöttöyrityksiä per minuutti
        int basePasses = 2 + (int) Math.round((avgTempo / 10.0) * 4);
        int passesThisMinute = basePasses + rand.nextInt(3);

        for (int i = 0; i < passesThisMinute; i++) {
            boolean isHomePossession = rand.nextInt(100) < match.getHomePossession();
            if (isHomePossession) {
                match.addHomePassCompleted();
                if (rand.nextDouble() < 0.05) match.addHomeThrowIn();
            } else {
                match.addAwayPassCompleted();
                if (rand.nextDouble() < 0.05) match.addAwayThrowIn();
            }
        }

        // 3. ERIKOISTAPAHTUMAT
        double eventRoll = rand.nextDouble();

        // A. LAUKAUS / MAALI / PUSKUMAALI / MAALIPOTKU (n. 8% chance/min)
        if (eventRoll < 0.080) {
            boolean isHomeAttacking = rand.nextBoolean();
            Team attackingTeam = isHomeAttacking ? home : away;
            Team defendingTeam = isHomeAttacking ? away : home;

            int redCardsDefending = countRedCards(defendingTeam);
            double redCardModifier = 1.0 + (redCardsDefending * 0.15);

            boolean hasManMarking = defendingTeam.getPlayers().stream()
                    .anyMatch(p -> p.getInstruction() == PlayerInstruction.MAN_MARKING && !p.isRedCard());

            boolean isOnTarget = rand.nextDouble() < 0.45;
            double shotXg = (0.05 + (rand.nextDouble() * 0.25)) * redCardModifier;

            if (isHomeAttacking) {
                match.addHomeShot(isOnTarget, shotXg);
            } else {
                match.addAwayShot(isOnTarget, shotXg);
            }

            if (isOnTarget && (!hasManMarking || rand.nextDouble() > 0.35)) {
                Player scorer = selectRandomAttacker(attackingTeam);

                if (isHomeAttacking) {
                    match.setHomeScore(match.getHomeScore() + 1);
                } else {
                    match.setAwayScore(match.getAwayScore() + 1);
                }

                boolean isHeader = rand.nextDouble() < 0.25;
                if (isHeader) {
                    match.addHeaderGoal();
                }

                String headerStr = isHeader ? " (Puskumaali!)" : "";
                MatchEvent goalEvent = new MatchEvent(
                        minute,
                        "⚽ MAALI! " + attackingTeam.getName() + " osuu!" + headerStr + " Tekijä: " + scorer.getName(),
                        EventType.GOAL,
                        attackingTeam
                );
                controller.onEventLogged(goalEvent);
                match.logEvent(goalEvent);

                processQueueSubstitutions(minute, EventType.GOAL);
            } else if (!isOnTarget) {
                if (isHomeAttacking) {
                    match.addAwayGoalKick();
                } else {
                    match.addHomeGoalKick();
                }
            }
        }
        // B. RIKE / KORTIT / RANGAISTUSPOTKU / VAPAAPOTKU (n. 6% chance/min)
        else if (eventRoll < 0.140) {
            boolean isHomeFouling = rand.nextBoolean();
            Team foulingTeam = isHomeFouling ? home : away;
            Team awardedTeam = isHomeFouling ? away : home;
            Player foulingPlayer = selectRandomFieldPlayer(foulingTeam);

            if (isHomeFouling) {
                match.addHomeFoul();
            } else {
                match.addAwayFoul();
            }

            if (referee.checkPenalty(1, rand)) {
                if (isHomeFouling) {
                    match.addAwayPenalty();
                    match.addAwayFreeKick();
                    match.setAwayScore(match.getAwayScore() + 1);
                } else {
                    match.addHomePenalty();
                    match.addHomeFreeKick();
                    match.setHomeScore(match.getHomeScore() + 1);
                }

                Player taker = selectRandomAttacker(awardedTeam);
                MatchEvent penaltyEvent = new MatchEvent(
                        minute,
                        "⚽ RANGAISTUSPOTKUMAALI! " + awardedTeam.getName() + " (Ampuja: " + taker.getName() + ")",
                        EventType.PENALTY,
                        awardedTeam
                );
                controller.onEventLogged(penaltyEvent);
                match.logEvent(penaltyEvent);

                processQueueSubstitutions(minute, EventType.PENALTY);
            } else {
                if (isHomeFouling) {
                    match.addAwayFreeKick();
                } else {
                    match.addHomeFreeKick();
                }

                if (foulingPlayer != null) {
                    foulingPlayer.commitFoul();
                    List<MatchEvent> tempLog = new ArrayList<>();

                    if (rand.nextDouble() < (referee.getStrictness() * 0.15)) {
                        referee.giveRedCard(foulingPlayer, foulingTeam, minute, "Ruma taklaus", tempLog);
                    } else if (rand.nextDouble() < (referee.getStrictness() * 0.80)) {
                        referee.giveYellowCard(foulingPlayer, foulingTeam, minute, tempLog);
                    }

                    for (MatchEvent cardEvent : tempLog) {
                        controller.onEventLogged(cardEvent);
                        match.logEvent(cardEvent);
                    }

                    if (referee.checkTeamForfeit(foulingTeam, minute, match.getMatchLog())) {
                        this.stopEngine();
                        return;
                    }
                }

                processQueueSubstitutions(minute, EventType.FREE_KICK);
            }
        }
        // C. KULMAPOTKU (n. 4% chance/min)
        else if (eventRoll < 0.180) {
            boolean isHomeCorner = rand.nextBoolean();
            Team cornerTeam = isHomeCorner ? home : away;

            if (isHomeCorner) match.addHomeCorner();
            else match.addAwayCorner();

            MatchEvent cornerEvent = new MatchEvent(
                    minute,
                    "🚩 KULMAPOTKU joukkueelle " + cornerTeam.getName(),
                    EventType.CORNER,
                    cornerTeam
            );
            controller.onEventLogged(cornerEvent);
            match.logEvent(cornerEvent);

            processQueueSubstitutions(minute, EventType.CORNER);
        }
        // D. PAITSIO (n. 2% chance/min)
        else if (eventRoll < 0.200) {
            boolean isHomeOffside = rand.nextBoolean();
            Team offsideTeam = isHomeOffside ? home : away;

            if (isHomeOffside) {
                match.addHomeOffside();
                match.addAwayFreeKick();
            } else {
                match.addAwayOffside();
                match.addHomeFreeKick();
            }

            MatchEvent offsideEvent = new MatchEvent(
                    minute,
                    "🚩 PAITSIO: " + offsideTeam.getName(),
                    EventType.FREE_KICK,
                    offsideTeam
            );
            controller.onEventLogged(offsideEvent);
            match.logEvent(offsideEvent);
        }
    }

    private void drainStaminaForTeam(Team team) {
        int redCards = countRedCards(team);
        double redCardPenalty = 1.0 + (redCards * 0.10);

        // Kova tempo väsyttää joukkuetta nopeammin
        double tempoDrain = (team.getTactic() != null) ? (0.7 + (team.getTactic().getTempo() / 10.0) * 0.6) : 1.0;

        for (Player p : team.getPlayers()) {
            if (p.isRedCard()) continue;

            double drain = 0.35 * redCardPenalty * tempoDrain;
            if (p.getInstruction() == PlayerInstruction.TIGHT_PRESSING) {
                drain *= 1.35;
            } else if (p.getInstruction() == PlayerInstruction.GET_FORWARD) {
                drain *= 1.15;
            }
            p.reduceStamina(drain);
        }
    }

    private int countRedCards(Team team) {
        int count = 0;
        for (Player p : team.getPlayers()) {
            if (p.isRedCard()) count++;
        }
        return count;
    }

    private Player selectRandomAttacker(Team team) {
        List<Player> players = team.getPlayers();
        if (players.isEmpty()) return new Player("Unknown", 50, 50, 50, 50, 50, 50, 50, 50, 50, 100.0, Positions.CB, 50, 50, 50, 50);

        for (int i = 0; i < 10; i++) {
            Player p = players.get(rand.nextInt(players.size()));
            if (!p.getName().toLowerCase().contains("maalivahti") && !p.isRedCard()) {
                return p;
            }
        }

        return players.stream().filter(p -> !p.isRedCard()).findFirst().orElse(players.get(0));
    }

    private Player selectRandomFieldPlayer(Team team) {
        List<Player> players = team.getPlayers();
        if (players.isEmpty()) return null;

        for (int i = 0; i < 10; i++) {
            Player p = players.get(rand.nextInt(players.size()));
            if (!p.isRedCard()) {
                return p;
            }
        }
        return players.stream().filter(p -> !p.isRedCard()).findFirst().orElse(null);
    }

    private void processQueueSubstitutions(int minute, EventType eventType) {
        List<SubstitutionQueue.Substitution> executed = match.getSubstitutionQueue().processSubstitutions(minute, eventType);
        for (SubstitutionQueue.Substitution sub : executed) {
            MatchEvent subEvent = new MatchEvent(
                    minute,
                    "🔄 VAIHTO: " + sub.getPlayerOut().getName() + " ⬇️ <--> " + sub.getPlayerIn().getName() + " ⬆️",
                    EventType.SUBSTITUTION,
                    sub.getTeam()
            );
            controller.onEventLogged(subEvent);
            match.logEvent(subEvent);
        }
    }

    private void checkAssistantCoachTriggers(int minute) {
        if (minute == 75) {
            int homeGoals = match.getHomeScore();
            int awayGoals = match.getAwayScore();

            if (homeGoals < awayGoals) {
                controller.onAssistantCoachPrompt(
                        "Olemme tappiolla (75 min)!",
                        "Nostetaanko prässiä ja hyökkäysmentaliteettia loppukiriä varten?",
                        () -> {
                            if (match.getHomeTeam().getTactic() != null) {
                                match.getHomeTeam().getTactic().setPressingIntensity(Math.min(10, match.getHomeTeam().getTactic().getPressingIntensity() + 3));
                                match.getHomeTeam().getTactic().setMentality(Math.min(10, match.getHomeTeam().getTactic().getMentality() + 3));
                            }
                        }
                );
            }
        }
    }

    private void triggerAssistantCoachHalfTime() {
        MatchEvent halfTimeEvent = new MatchEvent(45, "--- PUOLIAIKA --- Aikaa säätää taktiikoita", EventType.HALF_TIME, null);
        controller.onEventLogged(halfTimeEvent);
        match.logEvent(halfTimeEvent);
    }

    private void waitIfPaused() {
        while (isPaused && isRunning) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    public void pauseSimulation() { this.isPaused = true; }
    public void resumeSimulation() { this.isPaused = false; }
    public boolean isPaused() { return isPaused; }
    public void stopEngine() { this.isRunning = false; }
    public int getCurrentMinute() { return currentMinute; }
    public void setTickSpeedMs(int speedMs) { this.tickSpeedMs = speedMs; }
}