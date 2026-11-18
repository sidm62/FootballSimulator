package org.example;

import java.util.Scanner;

public class TacticsManager {

    /**
     * Mahdollistaa pelaajalle oman taktiikan luomisen syötteiden avulla.
     */
    public static Tactic createCustomTactic(Scanner scanner) {
        System.out.println("\n--- 🧠 LUO OMA TAKTIIKKA ---");

        System.out.print("Anna taktiikan nimi (esim. Oma Prässi): ");
        String name = scanner.nextLine();

        int mentality = readIntBetween(scanner, "Mentaalisuus (1 = Erittäin puolustava, 10 = Kaikki hyökkää): ", 1, 10);
        int passingStyle = readIntBetween(scanner, "Syöttötyyli (1 = Lyhytsyöttö/Tiki-Taka, 10 = Suoraviivainen pitkä pallo): ", 1, 10);
        int pressingIntensity = readIntBetween(scanner, "Prässin voimakkuus (1 = Bussitus/Matala block, 10 = Gegenpressing): ", 1, 10);
        int tempo = readIntBetween(scanner, "Pelin tempo (1 = Kontrolloitu hidas, 10 = Salama-vastalaukaukset): ", 1, 10);

        Tactic customTactic = new Tactic(name, mentality, passingStyle, pressingIntensity, tempo);
        System.out.println("✅ Uusi taktiikka '" + name + "' luotu onnistuneesti!\n");
        return customTactic;
    }

    /**
     * Mahdollistaa yksittäisen pelaajan erikoisohjeen (PlayerInstruction) asettamisen.
     */
    public static void assignPlayerInstruction(Player player, Scanner scanner) {
        System.out.println("\n--- MÄÄRITÄ PELAAN ERIKOISOHJE: " + player.getName() + " ---");
        System.out.println("1. NORMAL (Normaali rooli)");
        System.out.println("2. TIGHT_PRESSING (Tiukka prässi -> Kuluu enemmän staminaa)");
        System.out.println("3. GET_FORWARD (Nouse hyökkäykseen)");
        System.out.println("4. MAN_MARKING (Miesvartiointi -> Vähentää vastustajan maalimahdollisuuksia)");

        int choice = readIntBetween(scanner, "Valitse ohje (1-4): ", 1, 4);
        switch (choice) {
            case 2 -> player.setInstruction(PlayerInstruction.TIGHT_PRESSING);
            case 3 -> player.setInstruction(PlayerInstruction.GET_FORWARD);
            case 4 -> player.setInstruction(PlayerInstruction.MAN_MARKING);
            default -> player.setInstruction(PlayerInstruction.NONE);
        }
        System.out.println("✅ Pelaajalle " + player.getName() + " asetettu ohje: " + player.getInstruction());
    }

    private static int readIntBetween(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("⚠️ Arvon tulee olla välillä " + min + " - " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Anna kelvollinen numero.");
            }
        }
    }
}