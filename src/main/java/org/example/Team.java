package org.example;

import java.util.List;

public class Team {
    private String name;
    private List<Player> players;
    private List<Player> substitutes;
    private String formation;
    private Tactic tactic;
    private int substitutionsUsed = 0;


    public Team(String name, List<Player> players, List<Player> substitutes, String formation, Tactic tactic, int substitutionsUsed) {
        this.name = name;
        this.players = players;
        this.substitutes = substitutes;
        this.formation = formation;
        this.tactic = tactic;
        this.substitutionsUsed = substitutionsUsed;
    }

    public Team() {

    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<Player> getSubstitutes() {
        return substitutes;
    }

    public String getFormation() {
        return formation;
    }

    public Tactic getTactic() {
        return tactic;
    }
    public int getSubstitutionsUsed() {
        return substitutionsUsed;
    }

    public void incrementSubstitutionsUsed() {
        this.substitutionsUsed++;
    }

    public void setTactic(Tactic tactic) {
        this.tactic = tactic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setSubstitutes(List<Player> substitutes) {
        this.substitutes = substitutes;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }
}