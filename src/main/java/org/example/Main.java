package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Tactic posessionTactic = new Tactic("Possession", 7, 8, 6, 5);
        Tactic counterTactic = new Tactic("Counter Attack", 8, 6, 7, 8);

        // --- KOTIJOUKKUEEN AVAUSKOKOONPANO ---
        List<Player> homePlayers = new ArrayList<>();
        homePlayers.add(new Goalkeeper("K. Maalivahti", 28, 40, 20, 60, 40, 30, 80, 75, 70, 90, Positions.GK, 75, 65, 50, 70, 88, 85, 86, 87));
        homePlayers.add(new Player("P. Puolustaja 1", 24, 75, 30, 65, 55, 40, 82, 80, 78, 85, Positions.CB, 85, 60, 55, 75));
        homePlayers.add(new Player("P. Puolustaja 2", 27, 72, 25, 68, 52, 38, 84, 82, 80, 88, Positions.CB, 86, 58, 52, 74));
        homePlayers.add(new Player("L. Laitapakki 1", 22, 85, 45, 70, 72, 60, 75, 70, 65, 92, Positions.LB, 78, 70, 75, 80));
        homePlayers.add(new Player("R. Laitapakki 2", 25, 84, 42, 71, 70, 58, 76, 72, 66, 90, Positions.RB, 77, 68, 74, 79));
        homePlayers.add(new Player("C. Keskikenttä 1", 26, 70, 60, 85, 78, 72, 70, 68, 65, 88, Positions.CDM, 80, 82, 70, 82));
        homePlayers.add(new Player("C. Keskikenttä 2", 24, 74, 65, 88, 82, 75, 65, 65, 60, 86, Positions.CM, 72, 86, 78, 85));
        homePlayers.add(new Player("C. Keskikenttä 3", 29, 72, 70, 86, 84, 80, 60, 62, 58, 84, Positions.CAM, 65, 88, 80, 83));
        homePlayers.add(new Player("L. Laituri 1", 23, 90, 78, 75, 88, 82, 45, 60, 55, 85, Positions.LW, 40, 75, 82, 80));
        homePlayers.add(new Player("R. Laituri 2", 24, 91, 76, 76, 89, 81, 44, 59, 54, 86, Positions.RW, 39, 76, 83, 81));
        homePlayers.add(new Player("S. Hyökkääjä", 26, 86, 88, 70, 82, 89, 35, 72, 70, 87, Positions.ST, 30, 72, 65, 86));

        // Kotijoukkueen vaihdot
        List<Player> homeSubs = new ArrayList<>();
        homeSubs.add(new Goalkeeper("V. Varamaalivahti", 25, 40, 20, 55, 35, 25, 78, 72, 68, 95, Positions.GK, 72, 60, 45, 65, 85, 82, 83, 84));
        homeSubs.add(new Player("V. Varapuolustaja", 23, 78, 28, 62, 50, 35, 80, 78, 76, 95, Positions.CB, 82, 55, 50, 72));
        homeSubs.add(new Player("V. Varalaitapakki", 22, 83, 40, 68, 68, 55, 72, 68, 60, 95, Positions.LB, 74, 65, 70, 76));
        homeSubs.add(new Player("V. Varakeskikenttä 1", 26, 73, 60, 82, 76, 70, 68, 66, 62, 95, Positions.CM, 70, 82, 75, 80));
        homeSubs.add(new Player("V. Varakeskikenttä 2", 24, 76, 65, 84, 80, 74, 60, 62, 58, 95, Positions.CAM, 64, 84, 78, 81));
        homeSubs.add(new Player("V. Varalaituri", 21, 88, 74, 72, 85, 78, 42, 58, 52, 95, Positions.LW, 38, 72, 79, 78));
        homeSubs.add(new Player("V. Varahyökkääjä", 25, 84, 84, 68, 78, 85, 34, 68, 66, 95, Positions.ST, 28, 70, 62, 82));

        // --- VIERASJOUKKUEEN AVAUSKOKOONPANO ---
        List<Player> awayPlayers = new ArrayList<>();
        awayPlayers.add(new Goalkeeper("K. Maalivahti", 28, 40, 20, 60, 40, 30, 80, 75, 70, 90, Positions.GK, 75, 65, 50, 70, 88, 85, 86, 87));
        awayPlayers.add(new Player("V. Puolustaja 1", 29, 68, 28, 62, 50, 35, 85, 84, 82, 84, Positions.CB, 88, 55, 50, 72));
        awayPlayers.add(new Player("V. Puolustaja 2", 26, 70, 26, 64, 48, 36, 83, 81, 79, 86, Positions.CB, 84, 56, 51, 73));
        awayPlayers.add(new Player("V. Laitapakki 1", 24, 82, 40, 68, 66, 55, 74, 69, 62, 90, Positions.LB, 75, 65, 70, 78));
        awayPlayers.add(new Player("V. Laitapakki 2", 28, 80, 38, 67, 65, 54, 75, 70, 63, 88, Positions.RB, 76, 64, 69, 77));
        awayPlayers.add(new Player("V. Keskikenttä 1", 27, 73, 62, 80, 75, 70, 74, 72, 70, 89, Positions.CDM, 79, 78, 68, 84));
        awayPlayers.add(new Player("V. Keskikenttä 2", 25, 75, 64, 82, 78, 72, 70, 70, 66, 87, Positions.CM, 75, 80, 72, 82));
        awayPlayers.add(new Player("V. Keskikenttä 3", 26, 78, 66, 79, 80, 74, 62, 64, 60, 85, Positions.LW, 60, 77, 75, 79));
        awayPlayers.add(new Player("V. Keskikenttä 4", 28, 77, 65, 78, 79, 73, 63, 65, 61, 84, Positions.RW, 61, 76, 74, 78));
        awayPlayers.add(new Player("V. Hyökkääjä 1", 27, 84, 85, 68, 79, 84, 38, 70, 68, 85, Positions.ST, 32, 70, 60, 82));
        awayPlayers.add(new Player("V. Hyökkääjä 2", 24, 88, 82, 72, 83, 85, 36, 68, 65, 88, Positions.ST, 30, 73, 62, 85));

        // Vierasjoukkueen vaihdot
        List<Player> awaySubs = new ArrayList<>();
        awaySubs.add(new Goalkeeper("V. Varamaalivahti V", 26, 39, 19, 58, 38, 28, 77, 74, 69, 95, Positions.GK, 71, 58, 48, 68, 84, 81, 82, 83));
        awaySubs.add(new Player("V. Varapuolustaja V1", 28, 66, 26, 60, 48, 32, 82, 80, 78, 95, Positions.CB, 84, 52, 48, 70));
        awaySubs.add(new Player("V. Varapuolustaja V2", 25, 69, 25, 62, 46, 34, 81, 79, 76, 95, Positions.CB, 82, 54, 50, 71));
        awaySubs.add(new Player("V. Varalaitapakki V", 23, 80, 38, 66, 64, 52, 73, 68, 60, 95, Positions.LB, 72, 62, 68, 75));
        awaySubs.add(new Player("V. Varakeskikenttä V1", 26, 72, 60, 78, 74, 68, 72, 70, 68, 95, Positions.CDM, 76, 75, 65, 82));
        awaySubs.add(new Player("V. Varakeskikenttä V2", 24, 75, 63, 80, 76, 70, 68, 68, 64, 95, Positions.CM, 73, 78, 70, 80));
        awaySubs.add(new Player("V. Varahyökkääjä V", 26, 82, 83, 66, 76, 82, 36, 68, 66, 95, Positions.ST, 30, 68, 58, 80));

        // --- JOUKKUEIDEN LUOMINEN ---
        Team homeTeam = new Team("Koti Joukkue", homePlayers, homeSubs, "4-3-3", posessionTactic, 0);
        Team awayTeam = new Team("Vieras Joukkue", awayPlayers, awaySubs, "4-4-2", counterTactic, 0);

        // OPTIO: Mahdollistetaan valinnaisesti oman taktiikan luominen ennen ottelua
        System.out.print("Haluatko luoda uuden taktiikan kotijoukkueelle ennen ottelua? (k/e): ");
        String customChoice = scanner.nextLine();
        if (customChoice.equalsIgnoreCase("k")) {
            Tactic customTactic = TacticsManager.createCustomTactic(scanner);
            homeTeam.setTactic(customTactic);
        }

        Match match = new Match(homeTeam, awayTeam);
        GameController controller = new GameController(match);

        // Käynnistetään moottori omassa säikeessään
        controller.startMatch();

        System.out.println("\n🎮 OTTELU KÄYNNISSÄ!");
        System.out.println("Syötä komennoksi:");
        System.out.println(" 1 = Hidas nopeus (800 ms/min)");
        System.out.println(" 2 = Normaali nopeus (400 ms/min)");
        System.out.println(" 3 = Pikakelaus (10 ms/min)");
        System.out.println(" p = Tauko / Jatka");
        System.out.println(" q = Lopeta simulointi\n");

        // Luetaan näppäinsyötteitä lennosta pelinopeuden ja tauon säätämiseksi
        while (controller.getEngine() != null && controller.getEngine().getCurrentMinute() < 90) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                if (input.equals("1")) {
                    controller.getEngine().setTickSpeedMs(800);
                    System.out.println("⏱️ Pelinopeus asetettu: Hidas (800 ms/min)");
                } else if (input.equals("2")) {
                    controller.getEngine().setTickSpeedMs(400);
                    System.out.println("⏱️ Pelinopeus asetettu: Normaali (400 ms/min)");
                } else if (input.equals("3")) {
                    controller.getEngine().setTickSpeedMs(10);
                    System.out.println("⚡ Pelinopeus asetettu: Pikakelaus (10 ms/min)");
                } else if (input.equalsIgnoreCase("p")) {
                    if (controller.getEngine().isPaused()) {
                        controller.getEngine().resumeSimulation();
                        System.out.println("▶️ Ottelu jatkuu.");
                    } else {
                        controller.getEngine().pauseSimulation();
                        System.out.println("⏸️ Ottelu laitettu tauolle.");
                    }
                } else if (input.equalsIgnoreCase("q")) {
                    controller.getEngine().stopEngine();
                    System.out.println("🛑 Ottelusimulaatio keskeytetty.");
                    break;
                }
            }
        }

        System.out.println("\n🏁 Ottelu päättyi. Kiitos pelaamisesta!");
    }
}