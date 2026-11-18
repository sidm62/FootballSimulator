package org.example;

public enum Positions {
   ST ("Striker"),
   CF ("Center Forward"),
   LW ("Left Winger"),
   RW ("Right Winger"),
   CAM ("Central Attacking Midfielder"),
   CM ("Central Midfielder"),
   CDM ("Central Defensive Midfielder"),
   LB ("Left Back"),
   RB ("Right Back"),
   CB ("Center Back"),
   GK ("Goalkeeper");


   private final String positionName;

    Positions(String positionName) {
         this.positionName = positionName;
    }
    public String getPositionName() {
    return positionName;}
}
