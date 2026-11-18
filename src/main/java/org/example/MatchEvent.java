package org.example;

public class MatchEvent implements Comparable<MatchEvent> {

    private final int minute;
    private final String description;
    private final EventType eventType;
    private final Team team;


public MatchEvent(int minute, String description, EventType eventType, Team team) {
        this.minute = minute;
        this.description = description;
        this.eventType = eventType;
        this.team = team;
    }

    public int getMinute() {
        return minute;
    }

    public String getDescription() {
        return description;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {

        String teamInfo = (team != null) ? " (" + team.getName() + ")" : "";
        return "[" + minute + " min] " + eventType + ": " + description + teamInfo;
    }

    @Override
    public int compareTo(MatchEvent other) {
        return Integer.compare(this.minute, other.minute);
    }
}


