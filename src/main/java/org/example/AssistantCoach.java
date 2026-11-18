package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class AssistantCoach {
    private String name;

    public AssistantCoach(String name) {
        this.name = name;
    }
    Team team = new Team();

    // Tarkkailee kaikkia pelaajia, etsii väsyneimmät / kortin saaneet ja ehdottaa vaihtoa
    public void evaluatePlayerConditions(Team team, int currentMinute, Scanner scanner) {
        List<Player> players = team.getPlayers();
        if (players == null) return;

        // Etsitään kaikki pelaajat, jotka ovat väsyneitä (kunto < 60) eivätkä ole jo ulosajettuja tai vaihdettuja
        List<Player> candidates = new ArrayList<>();
        for (Player p : players) {
            if (!p.hasBeenSubbed() && !p.isRedCard() && p.getStamina() < 60) {
                candidates.add(p);
            }
        }

        // Jos kukaan ei ole väsynyt riittävästi, ei tehdä mitään
        if (candidates.isEmpty()) return;

        // Järjestetään ehdokkaat väsymyksen mukaan (pienin stamina ensin) tai satunnaistetaan hieman,
        // jotta vaihtelua tulee eri pelaajien välille
        candidates.sort((p1, p2) -> Integer.compare((int) p1.getStamina(), (int) p2.getStamina()));

        // Valitaan tarkasteltavaksi pahiten väsynyt (tai otetaan satunnaisesti joku pahiten väsyneistä)
        Player p = candidates.get(0);

        System.out.println("\n-------------------------------------------------------------------");
        System.out.println(" 🗣️ APUVALMENTAJA " + name + " [" + currentMinute + " min]:");
        System.out.println(" \"Boss, " + p.getName() + " (" + p.getPosition() + ") alkaa väsyä huomattavasti (kunto: " + p.getStamina() + "%).\"");

        if (p.getYellowCards() > 0) {
            System.out.println(" \"Hänellä on lisäksi keltainen alla, ulosajon riski on kasvanut merkittävästi!\"");
        }

        // Tarkistetaan onko 3 vaihtoa jo täynnä
        if (team.getSubstitutionsUsed() >= 3) {
            System.out.println(" -> [Huomio: Joukkue on jo tehnyt kaikki sallitut 3 vaihtoa! Vaihtoa ei voi tehdä.]");
            System.out.println("-------------------------------------------------------------------\n");
            return;
        }

        System.out.print(" Haluatko ottaa hänet vaihtoon? (k = kyllä, e = ei): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        boolean accepted = choice.equals("k") || choice.equals("kyllä") || choice.equals("joo");

        if (accepted) {
            List<Player> subs = team.getSubstitutes();
            if (subs == null || subs.isEmpty()) {
                System.out.println(" -> Vaihtopenkki on tyhjä! Vaihtoa ei voitu suorittaa.");
            } else {
                System.out.println("\n Saatavilla olevat vaihtopelaajat (Vaihtoja jäljellä: " + (3 - team.getSubstitutionsUsed()) + "/3):");
                for (int i = 0; i < subs.size(); i++) {
                    Player sub = subs.get(i);
                    System.out.println(" [" + (i + 1) + "] " + sub.getName() + " (" + sub.getPosition() + ", kunto: " + sub.getStamina() + "%)");
                }

                System.out.print(" Valitse kentälle tuleva pelaaja (syötä numero 1-" + subs.size() + "): ");
                try {
                    int subIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (subIndex >= 0 && subIndex < subs.size()) {
                        Player playerIn = subs.get(subIndex);

                        // Merkitään ulos menevä pelaaja vaihdetuksi
                        p.setHasBeenSubbed(true);

                        int pIndex = players.indexOf(p);

                        // Suoritetaan vaihto
                        players.set(pIndex, playerIn);
                        subs.remove(playerIn);
                        team.incrementSubstitutionsUsed();

                        System.out.println(" -> VAIHTO ONNISTUI! Ulos: " + p.getName() + " | Sisään: " + playerIn.getName() + " (Vaihtoja tehty: " + team.getSubstitutionsUsed() + "/3)");
                    } else {
                        System.out.println(" -> Virheellinen valinta. Vaihtoa ei tehty.");
                    }
                } catch (Exception e) {
                    System.out.println(" -> Virhe syötteessä. Vaihtoa ei tehty.");
                }
            }
        } else {
            System.out.println(" -> Päävalmentaja päätti pitää pelaajan kentällä omalla vastuulla.");
        }
        System.out.println("-------------------------------------------------------------------\n");
    }

    // Kysyy luvan taktiseen muutokseen (esim. 75. minuutilla jos ollaan tappiolla)
    public boolean askManagerForTacticalShift(Team team, int currentMinute, int goalDiff, Scanner scanner) {
        System.out.println("\n==================================================");
        System.out.println(" 📋 APUVALMENTAJA " + name + " (" + currentMinute + " min)");
        System.out.println("==================================================");

        if (goalDiff < 0) {
            System.out.println(" \"Olemme tappiolla! Ehdotan, että nostetaan prässiä ja hyökkäysmentaliteettia loppukiriä varten.\"");
        } else if (goalDiff > 1) {
            System.out.println(" \"Johtomme on vakaa. Ehdotan, että rauhoitetaan peliä ja varmistetaan tulosta.\"");
        } else {
            return false;
        }

        System.out.print(" Hyväksytkö muutoksen? (k = kyllä, e = ei): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        boolean accepted = choice.equals("k") || choice.equals("kyllä") || choice.equals("joo");

        if (accepted) {
            System.out.println(" -> Päävalmentaja hyväksyi muutoksen! Toteutetaan taktinen muutos.");
            return true;
        } else {
            System.out.println(" -> Päävalmentaja hylkäsi ehdotuksen. Jatketaan entisellä taktiikalla.");
            return false;
        }
    }
    public void analyzeSubandplayers() {

        List<Player> players = team.getPlayers();
        List<Player> substitutions = team.getSubstitutes();

       if (players == null || substitutions == null) {
           return;
       }
       for (Player player : players) {
           for (Player sub : substitutions) {
           if (player.getPosition() == sub.getPosition()) {

           }

           }
       }
    }

    public String getName() {
        return name;
    }
}