package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SubstitutionQueue {

    // Thread-safe lista, jotta UI (JavaFX) voi lisätä vaihtoja samaan aikaan kun simulaattorisäie lukee niitä
    private final List<Substitution> pendingSubstitutions = new CopyOnWriteArrayList<>();

    public SubstitutionQueue() {
    }

    /**
     * Lisää uuden vaihdon jonoon (esim. kun käyttäjä klikkaa UI-nappia)
     */
    public boolean addSubstitution(Team team, Player playerOut, Player playerIn, int currentMinute) {
        // Estetään saman pelaajan vaihtaminen useasti tai jo kentällä olevan tilalle vaihtaminen virheellisesti
        if (playerOut == null || playerIn == null || playerOut.equals(playerIn)) {
            return false;
        }

        // Tarkistetaan onko pelaaja Out jo jonossa
        for (Substitution sub : pendingSubstitutions) {
            if (sub.getPlayerOut().equals(playerOut)) {
                return false; // Vaihto on jo jonossa!
            }
        }

        pendingSubstitutions.add(new Substitution(team, playerOut, playerIn, currentMinute));
        return true;
    }

    /**
     * Käsittelee jonossa olevat vaihdot pelikatkon (Event) sattuessa.
     * Palauttaa listan toteutetuista vaihdoista tapahtumalokia ja UI-päivitystä varten.
     */
    public List<Substitution> processSubstitutions(int currentMinute, EventType eventType) {
        List<Substitution> executedSubstitutions = new ArrayList<>();

        // Vaihdot tehdään vain kun pallo on poissa pelistä
        if (!isBallOutOfPlay(eventType) || pendingSubstitutions.isEmpty()) {
            return executedSubstitutions;
        }

        for (Substitution sub : pendingSubstitutions) {
            Team team = sub.getTeam();
            Player out = sub.getPlayerOut();
            Player in = sub.getPlayerIn();

            // Varmistetaan että pelaaja out on edelleen kentällä ja pelaaja in on vaihtopenkillä
            if (team.getPlayers().contains(out) && team.getSubstitutes().contains(in)) {

                // Toteutetaan kokoonpanomuutos
                team.getPlayers().remove(out);
                team.getPlayers().add(in);
                team.getSubstitutes().remove(in);
                team.getSubstitutes().add(out); // Siirretään poistunut pelaaja penkille

                executedSubstitutions.add(sub);
                pendingSubstitutions.remove(sub);
            }
        }

        return executedSubstitutions;
    }

    /**
     * Määrittelee mitkä tapahtumat lasketaan pelikatkoiksi, jolloin vaihto voidaan suorittaa.
     */
    private boolean isBallOutOfPlay(EventType eventType) {
        return eventType == EventType.GOAL ||
                eventType == EventType.CORNER ||
                eventType == EventType.FREE_KICK ||
                eventType == EventType.GOAL_KICK ||
                eventType == EventType.PENALTY ||
                eventType == EventType.HALF_TIME;
    }

    /**
     * Tyhjentää tietyn joukkueen jonossa olevat vaihdot (jos käyttäjä esim. peruuttaa vaihdot GUI:ssa)
     */
    public void cancelPendingSubstitutionsForTeam(Team team) {
        pendingSubstitutions.removeIf(sub -> sub.getTeam().equals(team));
    }

    public List<Substitution> getPendingSubstitutions() {
        return pendingSubstitutions;
    }

    public boolean hasPendingSubstitutions() {
        return !pendingSubstitutions.isEmpty();
    }


    public static class Substitution {
        private final Team team;
        private final Player playerOut;
        private final Player playerIn;
        private final int requestedMinute;

        public Substitution(Team team, Player playerOut, Player playerIn, int requestedMinute) {
            this.team = team;
            this.playerOut = playerOut;
            this.playerIn = playerIn;
            this.requestedMinute = requestedMinute;
        }

        public Team getTeam() {
            return team;
        }

        public Player getPlayerOut() {
            return playerOut;
        }

        public Player getPlayerIn() {
            return playerIn;
        }

        public int getRequestedMinute() {
            return requestedMinute;
        }
    }
}