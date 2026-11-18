package org.example;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private final Team homeTeam;
    private final Team awayTeam;
    private Referee referee;

    private int homeScore = 0;
    private int awayScore = 0;

    private double homeXg = 0.0;
    private double awayXg = 0.0;

    // --- TILASTOT ---
    private int homePossession = 50;
    private int awayPossession = 50;

    private int homeShots = 0;
    private int awayShots = 0;
    private int homeShotsOnTarget = 0;
    private int awayShotsOnTarget = 0;

    private int homeCorners = 0;
    private int awayCorners = 0;
    private int homeFouls = 0;
    private int awayFouls = 0;
    private int homePassesCompleted = 0;
    private int awayPassesCompleted = 0;

    private int homeYellowCards = 0;
    private int awayYellowCards = 0;
    private int homeRedCards = 0;
    private int awayRedCards = 0;

    private int homeThrowIns = 0;
    private int awayThrowIns = 0;
    private int homeGoalKicks = 0;
    private int awayGoalKicks = 0;
    private int homeFreeKicks = 0;
    private int awayFreeKicks = 0;
    private int homeOffsides = 0;
    private int awayOffsides = 0;

    private int homePenalties = 0;
    private int awayPenalties = 0;
    private int headers = 0;

    private final List<MatchEvent> matchLog = new ArrayList<>();
    private final SubstitutionQueue substitutionQueue = new SubstitutionQueue();

    private final double HOME_ADVANTAGE_MULTIPLIER = 1.08;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.referee = new Referee("Jari Tuomari", 0.18);
        resetMatch();
    }

    public Match(Team homeTeam, Team awayTeam, Referee referee) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.referee = referee;
        resetMatch();
    }

    /**
     * Valmistelee ottelun alustamalla pelaajat ja laskemalla alustavat arvot.
     */
    public void prepareMatch() {
        resetMatch();

        double homeAtk = calculateTeamAttack(homeTeam) * HOME_ADVANTAGE_MULTIPLIER;
        double homeDef = calculateTeamDefense(homeTeam) * HOME_ADVANTAGE_MULTIPLIER;
        double awayAtk = calculateTeamAttack(awayTeam);
        double awayDef = calculateTeamDefense(awayTeam);

        // Pallonhallinta suhteutettuna taktiikoihin ja hyökkäysvoimaan
        int homePassingStyle = (homeTeam.getTactic() != null) ? homeTeam.getTactic().getPassingStyle() : 5;
        int awayPassingStyle = (awayTeam.getTactic() != null) ? awayTeam.getTactic().getPassingStyle() : 5;
        double homePower = homeAtk * homePassingStyle;
        double awayPower = awayAtk * awayPassingStyle;

        this.homePossession = (int) Math.round((homePower / (homePower + awayPower)) * 100);
        this.awayPossession = 100 - this.homePossession;
    }

    private void resetMatch() {
        resetTeamPlayers(homeTeam);
        resetTeamPlayers(awayTeam);
        this.homeScore = 0;
        this.awayScore = 0;
        this.homeXg = 0.0;
        this.awayXg = 0.0;
        this.homeShots = 0;
        this.awayShots = 0;
        this.homeShotsOnTarget = 0;
        this.awayShotsOnTarget = 0;
        this.homeCorners = 0;
        this.awayCorners = 0;
        this.homeFouls = 0;
        this.awayFouls = 0;
        this.homePassesCompleted = 0;
        this.awayPassesCompleted = 0;
        this.homeThrowIns = 0;
        this.awayThrowIns = 0;
        this.homeGoalKicks = 0;
        this.awayGoalKicks = 0;
        this.homeFreeKicks = 0;
        this.awayFreeKicks = 0;
        this.homeOffsides = 0;
        this.awayOffsides = 0;
        this.homePenalties = 0;
        this.awayPenalties = 0;
        this.headers = 0;
        this.matchLog.clear();
    }

    private void resetTeamPlayers(Team team) {
        if (team.getPlayers() != null) {
            for (Player p : team.getPlayers()) {
                p.resetForNewMatch();
            }
        }
        if (team.getSubstitutes() != null) {
            for (Player p : team.getSubstitutes()) {
                p.resetForNewMatch();
            }
        }
    }

    public void logEvent(MatchEvent event) {
        this.matchLog.add(event);
    }

    /**
     * Tulostaa lopputilastot konsoliin reaaliaikaisen moottorin päätyttyä.
     */
    public void printMatchSummary() {
        this.homeYellowCards = countTeamYellowCards(homeTeam);
        this.awayYellowCards = countTeamYellowCards(awayTeam);
        this.homeRedCards = countTeamRedCards(homeTeam);
        this.awayRedCards = countTeamRedCards(awayTeam);

        System.out.println("\n========================================");
        System.out.println("            OTTELU PÄÄTTYI              ");
        System.out.println("========================================");
        System.out.println(" " + homeTeam.getName() + "  " + homeScore + " - " + awayScore + "  " + awayTeam.getName());
        System.out.println("----------------------------------------");
        System.out.println("TILASTOT:");
        System.out.println("Pallonhallinta:            " + homePossession + "% - " + awayPossession + "%");
        System.out.println("Laukaukset (Kohti maalia): " + homeShots + " (" + homeShotsOnTarget + ") - " + awayShots + " (" + awayShotsOnTarget + ")");
        System.out.println("Puskumaalit (Headers):     " + headers);
        System.out.printf("Maaliodottama (xG):        %.2f - %.2f\n", homeXg, awayXg);
        System.out.println("Kulmapotkut:               " + homeCorners + " - " + awayCorners);
        System.out.println("Sivurajat (Throw-ins):     " + homeThrowIns + " - " + awayThrowIns);
        System.out.println("Maalipotkut (Goal kicks):  " + homeGoalKicks + " - " + awayGoalKicks);
        System.out.println("Vapaapotkut (Free kicks):  " + homeFreeKicks + " - " + awayFreeKicks);
        System.out.println("Paitsiot (Offsides):       " + homeOffsides + " - " + awayOffsides);
        System.out.println("Rangaistuspotkut:          " + homePenalties + " - " + awayPenalties);
        System.out.println("Onnistuneet syötöt:        " + homePassesCompleted + " - " + awayPassesCompleted);
        System.out.println("Rikkeet:                   " + homeFouls + " - " + awayFouls);
        System.out.println("Keltaiset kortit:          " + homeYellowCards + " - " + awayYellowCards);
        System.out.println("Punaiset kortit:           " + homeRedCards + " - " + awayRedCards);
        System.out.println("========================================");
    }

    private double calculateTeamAttack(Team team) {
        List<Player> players = team.getPlayers();
        if (players == null || players.isEmpty()) return 50.0;
        double total = 0;
        for (Player p : players) {
            total += (p.getFinishing() * 0.35) + (p.getShooting() * 0.25) + (p.getDribbling() * 0.20) + (p.getPassing() * 0.20);
        }
        return total / players.size();
    }

    private double calculateTeamDefense(Team team) {
        List<Player> players = team.getPlayers();
        if (players == null || players.isEmpty()) return 50.0;
        double total = 0;
        for (Player p : players) {
            total += (p.getDefending() * 0.40) + (p.getTackling() * 0.35) + (p.getPhysicality() * 0.25);
        }
        return total / players.size();
    }

    private int countTeamYellowCards(Team team) {
        int count = 0;
        if (team.getPlayers() != null) {
            for (Player p : team.getPlayers()) {
                count += p.getYellowCards();
            }
        }
        return count;
    }

    private int countTeamRedCards(Team team) {
        int count = 0;
        if (team.getPlayers() != null) {
            for (Player p : team.getPlayers()) {
                if (p.isRedCard()) count++;
            }
        }
        return count;
    }

    // --- GETTERIT JA SETTERIT / TILASTOMETODIT ---

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public Referee getReferee() { return referee; }
    public void setReferee(Referee referee) { this.referee = referee; }

    public int getHomeScore() { return homeScore; }
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }
    public int getAwayScore() { return awayScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    public double getHomeXg() { return homeXg; }
    public double getAwayXg() { return awayXg; }
    public int getHomePossession() { return homePossession; }
    public int getAwayPossession() { return awayPossession; }

    public List<MatchEvent> getMatchLog() { return matchLog; }
    public SubstitutionQueue getSubstitutionQueue() { return substitutionQueue; }

    // Tapahtumalaskurit dynaamisilla xG- ja tilastopäivityksillä
    public void addHomeShot(boolean onTarget, double xg) {
        this.homeShots++;
        if (onTarget) this.homeShotsOnTarget++;
        this.homeXg += xg;
    }
    public void addAwayShot(boolean onTarget, double xg) {
        this.awayShots++;
        if (onTarget) this.awayShotsOnTarget++;
        this.awayXg += xg;
    }

    public void addHomeCorner() { this.homeCorners++; }
    public void addAwayCorner() { this.awayCorners++; }
    public void addHomeFoul() { this.homeFouls++; }
    public void addAwayFoul() { this.awayFouls++; }
    public void addHomeOffside() { this.homeOffsides++; }
    public void addAwayOffside() { this.awayOffsides++; }
    public void addHomeFreeKick() { this.homeFreeKicks++; }
    public void addAwayFreeKick() { this.awayFreeKicks++; }
    public void addHomePenalty() { this.homePenalties++; }
    public void addAwayPenalty() { this.awayPenalties++; }
    public void addHeaderGoal() { this.headers++; }

    // Täydennetyt syöttö-, sivuraja- ja maalipotkulaskurit
    public void addHomePassCompleted() { this.homePassesCompleted++; }
    public void addAwayPassCompleted() { this.awayPassesCompleted++; }
    public void addHomeThrowIn() { this.homeThrowIns++; }
    public void addAwayThrowIn() { this.awayThrowIns++; }
    public void addHomeGoalKick() { this.homeGoalKicks++; }
    public void addAwayGoalKick() { this.awayGoalKicks++; }
}