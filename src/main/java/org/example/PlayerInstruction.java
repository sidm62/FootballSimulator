package org.example;

public enum PlayerInstruction {
    NONE("Normaali rooli"),
    MAN_MARKING("Miesvartiointi"),
    TIGHT_PRESSING("Aggressiivinen prässi"),
    GET_FORWARD("Nouse voimakkaasti hyökkäyksiin"),
    HOLD_POSITION("Pysy pelipaikallasi");

    private final String description;

    PlayerInstruction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}