package org.example;

public class Player {
    private String name;
    private int age;
    private int speed;
    private int finishing;
    private int passing;
    private int dribbling;
    private int shooting;
    private int defending;
    private int physicality;
    private int heading;

    // Muutettu double-tyypiksi, jotta stamina kuluu tarkasti desimaaleina (esim. 0.35 / min)
    private double stamina;

    private Positions position;
    private int tackling;
    private int vision;
    private int crossing;
    private int workRate;

    private int foulsCommitted = 0;
    private int yellowCards = 0;
    private boolean isRedCard = false;
    private boolean hasBeenSubbed = false;

    // Taktiset ohjeet
    private PlayerInstruction instruction = PlayerInstruction.NONE;
    private Player markedTarget = null;
    private Player pressureTarget = null;
    private Player attackTarget = null;

    public Player(String name, int age, int speed, int finishing, int passing, int dribbling, int shooting, int defending, int physicality, int heading, double stamina, Positions position, int tackling, int vision, int crossing, int workRate) {
        this.name = name;
        this.age = age;
        this.speed = speed;
        this.finishing = finishing;
        this.passing = passing;
        this.dribbling = dribbling;
        this.shooting = shooting;
        this.defending = defending;
        this.physicality = physicality;
        this.heading = heading;
        this.stamina = stamina;
        this.position = position;
        this.tackling = tackling;
        this.vision = vision;
        this.crossing = crossing;
        this.workRate = workRate;
    }

    // --- RIKKEET JA KORTIT ---

    public void commitFoul() {
        this.foulsCommitted++;
    }

    public void giveYellowCard() {
        this.yellowCards++;
        if (this.yellowCards >= 2) {
            this.isRedCard = true; // Toinen keltainen kortti -> ulosajo
        }
    }

    public void forceRedCard() {
        this.isRedCard = true; // Suora punainen kortti
    }

    public int getFoulsCommitted() {
        return foulsCommitted;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public boolean isRedCard() {
        return isRedCard;
    }

    // --- KUNTO / VÄSYMYS ---

    public void reduceStamina(double amount) {
        this.stamina = Math.max(0.0, this.stamina - amount);
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    // --- OTTELUN NOLLAUS ---

    public void resetForNewMatch() {
        this.stamina = 100.0;
        this.hasBeenSubbed = false;
        this.yellowCards = 0;
        this.isRedCard = false;
        this.foulsCommitted = 0;
        clearInstruction(); // Nollataan myös edellisen ottelun taktiset erikoiskäskyt!
    }

    // --- TAKTISET OHJEET JA KOHDETIKETIT ---

    public void setInstruction(PlayerInstruction instruction) {
        this.instruction = instruction;
    }

    public PlayerInstruction getInstruction() {
        return instruction;
    }

    public void setMarkedTarget(Player target) {
        this.instruction = PlayerInstruction.MAN_MARKING;
        this.markedTarget = target;
    }

    public Player getMarkedTarget() {
        return markedTarget;
    }

    public void setPressureTarget(Player target) {
        this.instruction = PlayerInstruction.TIGHT_PRESSING;
        this.pressureTarget = target;
    }

    public Player getPressureTarget() {
        return pressureTarget;
    }

    public void setAttackTarget(Player target) {
        this.instruction = PlayerInstruction.GET_FORWARD;
        this.attackTarget = target;
    }

    public Player getAttackTarget() {
        return attackTarget;
    }

    public void clearInstruction() {
        this.instruction = PlayerInstruction.NONE;
        this.markedTarget = null;
        this.pressureTarget = null;
        this.attackTarget = null;
    }

    // --- GEETTERIT JA SETTERIT ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getFinishing() { return finishing; }
    public void setFinishing(int finishing) { this.finishing = finishing; }

    public int getPassing() { return passing; }
    public void setPassing(int passing) { this.passing = passing; }

    public int getDribbling() { return dribbling; }
    public void setDribbling(int dribbling) { this.dribbling = dribbling; }

    public int getShooting() { return shooting; }
    public void setShooting(int shooting) { this.shooting = shooting; }

    public int getDefending() { return defending; }
    public void setDefending(int defending) { this.defending = defending; }

    public int getPhysicality() { return physicality; }
    public void setPhysicality(int physicality) { this.physicality = physicality; }

    public int getHeading() { return heading; }
    public void setHeading(int heading) { this.heading = heading; }

    public Positions getPosition() { return position; }
    public void setPosition(Positions position) { this.position = position; }

    public int getTackling() { return tackling; }
    public void setTackling(int tackling) { this.tackling = tackling; }

    public int getVision() { return vision; }
    public void setVision(int vision) { this.vision = vision; }

    public int getCrossing() { return crossing; }
    public void setCrossing(int crossing) { this.crossing = crossing; }

    public int getWorkRate() { return workRate; }
    public void setWorkRate(int workRate) { this.workRate = workRate; }

    public boolean hasBeenSubbed() { return hasBeenSubbed; }
    public void setHasBeenSubbed(boolean hasBeenSubbed) { this.hasBeenSubbed = hasBeenSubbed; }
}