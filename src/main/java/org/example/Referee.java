package org.example;

import java.util.List;
import java.util.Random;

public class Referee {
    private String name;
    private double strictness;

    public Referee(String name, double strictness) {
        this.name = name;
        this.strictness = strictness;
    }

    public void evaluateFoulsAndCards(Team team, int totalFouls, int minStart, int minEnd, Random rand, List<MatchEvent> matchLog) {
        List<Player> players = team.getPlayers();
        if (players == null || players.isEmpty()) return;

        for (int i = 0; i < totalFouls; i++) {
            Player player = getRandomFieldPlayer(players, rand);
            if (player != null) {
                player.commitFoul();
                int minute = minStart + rand.nextInt(minEnd - minStart + 1);

                // Suoran punaisen todennäköisyyttä nostettu hieman (0.03 -> 0.08)
                if (rand.nextDouble() < (strictness * 0.08)) {
                    giveRedCard(player, team, minute, "Ruma taklaus", matchLog);
                } else if (rand.nextDouble() < (strictness * 0.45)) {
                    giveYellowCard(player, team, minute, matchLog);
                }
            }
        }
    }

    public void giveYellowCard(Player player, Team team, int minute, List<MatchEvent> matchLog) {
        if (player == null || player.isRedCard()) return;

        player.giveYellowCard();
        String desc = "🟨 KELTAINEN KORTTI (" + this.name + "): " + player.getName() + " (" + team.getName() + ")";
        matchLog.add(new MatchEvent(minute, desc, EventType.YELLOW_CARD, team));

        // Tarkistetaan johtiko toiseen keltaiseen -> punainen
        if (player.isRedCard()) {
            String redDesc = "🟥 PUNAINEN KORTTI! Toinen keltainen johtaa ulosajoon: " + player.getName() + " (" + team.getName() + ")";
            matchLog.add(new MatchEvent(minute, redDesc, EventType.RED_CARD, team));
        }
    }

    public void giveRedCard(Player player, Team team, int minute, String reason, List<MatchEvent> matchLog) {
        if (player == null || player.isRedCard()) return;

        player.forceRedCard();
        String desc = "🟥 SUORA PUNAINEN KORTTI (" + this.name + "): " + player.getName() + " (" + team.getName() + ") - Syy: " + reason;
        matchLog.add(new MatchEvent(minute, desc, EventType.RED_CARD, team));
    }

    public boolean checkPenalty(int fouls, Random rand) {
        double penaltyChance = fouls * 0.025 * (strictness * 2.5);
        return rand.nextDouble() < penaltyChance;
    }

    public boolean checkTeamForfeit(Team team, int minute, List<MatchEvent> matchLog) {
        List<Player> players = team.getPlayers();
        if (players == null || players.isEmpty()) return false;

        int redCardCount = 0;
        for (Player p : players) {
            if (p.isRedCard()) redCardCount++;
        }

        if (redCardCount >= 3) {
            String desc = "🚨 OTTELU KESKEYTETÄÄN - LUOVUTUSVOITTO (" + team.getName() + " sai 5 punaista korttia)!";
            matchLog.add(new MatchEvent(minute, desc, EventType.FULL_TIME, team));
            return true;
        }
        return false;
    }

    private Player getRandomFieldPlayer(List<Player> players, Random rand) {
        int attempts = 0;
        while (attempts < 20) {
            Player p = players.get(rand.nextInt(players.size()));
            if (!p.getName().toLowerCase().contains("maalivahti") && !p.isRedCard()) {
                return p;
            }
            attempts++;
        }
        return null;
    }


    public String getName() { return name; }
    public double getStrictness() { return strictness; }
}