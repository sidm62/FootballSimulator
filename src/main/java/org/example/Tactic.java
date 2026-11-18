package org.example;

public class Tactic {
    private String name;
    private int mentality;         // 1 (Hyökkäävä) - 10 (Puolustava)
    private int passingStyle;      // 1 (Lyhyt) - 10 (Pitkä syöttö)
    private int pressingIntensity; // 1 (Matala) - 10 (Korkea prässi)
    private int tempo;             // 1 (Hidas control) - 10 (Pikahyökkäys)

    public Tactic(String name, int mentality, int passingStyle, int pressingIntensity, int tempo) {
        this.name = name;
        this.mentality = Math.max(1, Math.min(10, mentality));
        this.passingStyle = Math.max(1, Math.min(10, passingStyle));
        this.pressingIntensity = Math.max(1, Math.min(10, pressingIntensity));
        this.tempo = Math.max(1, Math.min(10, tempo));
    }



    // Getterit ja Setterit
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMentality() { return mentality; }
    public void setMentality(int mentality) { this.mentality = Math.max(1, Math.min(10, mentality)); }

    public int getPassingStyle() { return passingStyle; }
    public void setPassingStyle(int passingStyle) { this.passingStyle = Math.max(1, Math.min(10, passingStyle)); }

    public int getPressingIntensity() { return pressingIntensity; }
    public void setPressingIntensity(int pressingIntensity) { this.pressingIntensity = Math.max(1, Math.min(10, pressingIntensity)); }

    public int getTempo() { return tempo; }
    public void setTempo(int tempo) { this.tempo = Math.max(1, Math.min(10, tempo)); }
}