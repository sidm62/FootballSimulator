package org.example;

public class Goalkeeper extends Player {

    private int reflexes;
    private int handling;
    private int positioning;
    private int diving;


    public Goalkeeper(String name, int age, int speed, int finishing, int passing, int dribbling,
                      int shooting, int defending, int physicality, int heading, int stamina,
                      Positions position, int tackling, int vision, int crossing, int workRate,
                      int reflexes, int handling, int positioning, int diving) {


        super(name, age, speed, finishing, passing, dribbling, shooting, defending,
                physicality, heading, stamina, position, tackling, vision, crossing, workRate);


        this.reflexes = reflexes;
        this.handling = handling;
        this.positioning = positioning;
        this.diving = diving;
    }

    @Override
    public void reduceStamina(double amount) {

        super.reduceStamina(amount / 6);
    }


    public int getReflexes() { return reflexes; }
    public int getHandling() { return handling; }
    public int getPositioning() { return positioning; }
    public int getDiving() { return diving; }


    public void setReflexes(int reflexes) { this.reflexes = reflexes; }
    public void setHandling(int handling) { this.handling = handling; }
    public void setPositioning(int positioning) { this.positioning = positioning; }
    public void setDiving(int diving) { this.diving = diving; }
}