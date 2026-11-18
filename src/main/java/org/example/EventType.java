package org.example;

public enum EventType {

    // --- Ottelun kulku ja aika ---
    KICK_OFF("Ottelu alkaa", Category.MATCH_FLOW),
    HALF_TIME("Puoliaika", Category.MATCH_FLOW),
    FULL_TIME("Ottelu päättyi", Category.MATCH_FLOW),
    INJURY_TIME("Lisäaika", Category.MATCH_FLOW),
    EXTRA_TIME("Jatkoaika", Category.MATCH_FLOW),
    PENALTY_SHOOTOUT("Rangaistuspotkukilpailu", Category.MATCH_FLOW),

    // --- Tärkeät pelitapahtumat ---
    GOAL("Maali", Category.MAJOR_EVENT),
    PENALTY("Rangaistuspotku", Category.MAJOR_EVENT),
    SUBSTITUTION("Vaihto", Category.MAJOR_EVENT),
    INJURY("Loukkaantuminen", Category.MAJOR_EVENT),

    // --- Tuomarin päätökset & kortit ---
    FOUL("Rike", Category.DISCIPLINE),
    YELLOW_CARD("Keltainen kortti", Category.DISCIPLINE),
    RED_CARD("Punainen kortti", Category.DISCIPLINE),
    OFFSIDE("Paitsio", Category.DISCIPLINE),

    // --- Laukaukset & Torjunnat ---
    SHOT_ON_TARGET("Laukaus kohti maalia", Category.ATTACK),
    SHOT_OFF_TARGET("Laukaus ohi maalin", Category.ATTACK),
    SAVE("Torjunta", Category.ATTACK),
    HEADER("Puskumaali / Pusku", Category.ATTACK),

    // --- Erikoistilanteet & Katkot ---
    CORNER("Kulmapotku", Category.SET_PIECE),
    FREE_KICK("Vapaapotku", Category.SET_PIECE),
    THROW_IN("Sivurajaheitto", Category.SET_PIECE),
    GOAL_KICK("Maalipotku", Category.SET_PIECE),

    // --- Syötöt & Pallonhallinta ---
    PASS_COMPLETE("Onnistunut syöttö", Category.PASSING),
    PASS_INCOMPLETE("Erehtynyt syöttö", Category.PASSING),
    CROSS("Keskitys", Category.PASSING),
    DRIBBLE("Harhautus / Kuljetus", Category.ACTION),
    TACKLE("Taklaus", Category.DEFENDING),
    POSSESSION_GAIN("Pallonriisto", Category.DEFENDING),
    POSSESSION_LOSS("Pallonmenetys", Category.ATTACK);

    private final String displayName;
    private final Category category;

    EventType(String displayName, Category category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Category getCategory() {
        return category;
    }

    public enum Category {
        MATCH_FLOW,
        MAJOR_EVENT,
        DISCIPLINE,
        ATTACK,
        SET_PIECE,
        PASSING,
        ACTION,
        DEFENDING
    }
}