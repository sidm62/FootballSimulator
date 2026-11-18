package org.example;

import java.util.Scanner;

public class GameController {

    private final Match match;
    private MatchEngine engine;
    private final Scanner scanner = new Scanner(System.in);

    public GameController(Match match) {
        this.match = match;
    }

    /**
     * Käynnistää ottelumoottorin omassa taustasäikeessään.
     */
    public void startMatch() {
        this.engine = new MatchEngine(match, this);
        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    // ==========================================
    // MATCHENGINEN KUTSUMAT METODIT
    // ==========================================

    /**
     * Päivittää pelikellon tilanteen minuuteittain.
     */
    public void onMinuteTick(int minute, int homeScore, int awayScore) {
        if (minute % 15 == 0) {
            System.out.println("⏱️  [" + minute + " min] " + match.getHomeTeam().getName() + " "
                    + homeScore + " - " + awayScore + " " + match.getAwayTeam().getName());
        }
    }

    /**
     * Tulostaa reaaliaikaisen tapahtuman konsoliin.
     */
    public void onEventLogged(MatchEvent event) {
        System.out.println(event.toString());
    }

    /**
     * Reagoi apuvalmentajan taktisiin ehdotuksiin ottelun aikana.
     */
    public void onAssistantCoachPrompt(String title, String message, Runnable onApprove) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("📋 APUVALMENTAJA: " + title);
        System.out.println(message);
        System.out.print("Hyväksytäänkö taktinen muutus? (k/e): ");

        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("k") || input.equals("kyllä") || input.equals("y")) {
            onApprove.run();
            System.out.println("✅ Taktinen muutos suoritettu voimaan välittömästi!");
        } else {
            System.out.println("❌ Jatketaan nykyisellä taktiikalla.");
        }
        System.out.println("--------------------------------------------------\n");
    }

    // ==========================================
    // KÄYTTÄJÄN TOIMINNOT (TAKTIKAT & VAIHTOJONO)
    // ==========================================

    /**
     * Jonottaa pelaajavaihdon tapahtuvaksi seuraavalla pelikatkolla.
     */
    public boolean queueSubstitution(Team team, Player playerOut, Player playerIn) {
        if (engine == null) return false;

        int currentMin = engine.getCurrentMinute();
        boolean queued = match.getSubstitutionQueue().addSubstitution(team, playerOut, playerIn, currentMin);

        if (queued) {
            System.out.println("🔄 [VAIHTOJONO]: Pyyntö jonotettu minuutilla " + currentMin
                    + " (" + playerOut.getName() + " ⬇️ <--> " + playerIn.getName() + " ⬆️)");
        } else {
            System.out.println("⚠️ [VAIHTOJONO]: Vaihtoa ei voitu lisätä jonoon.");
        }
        return queued;
    }

    /**
     * Päivittää joukkueen taktiikkaa lennosta peliä keskeyttämättä.
     */
    public void updateTactics(Team team, int mentality, int pressing) {
        if (team.getTactic() != null) {
            team.getTactic().setMentality(mentality);
            team.getTactic().setPressingIntensity(pressing);
            System.out.println("📋 [TAKTIKKA]: " + team.getName() + " päivitti taktiikan -> Mentaliteetti: "
                    + mentality + ", Prässi: " + pressing);
        }
    }

    /**
     * Tauottaa tai jatkaa ottelun simulointia.
     */
    public void togglePause() {
        if (engine != null) {
            if (engine.isPaused()) {
                engine.resumeSimulation();
                System.out.println("▶️ Ottelua jatketaan.");
            } else {
                engine.pauseSimulation();
                System.out.println("⏸️ Ottelu laitettu tauolle.");
            }
        }
    }

    public Match getMatch() {
        return match;
    }

    public MatchEngine getEngine() {
        return engine;
    }
}