package com.mmocrpg.player;

public enum PlayerClass {

    WARRIOR("Warrior", "§c", 15, 8, 5, 12, 5, 1.5, 0.8),
    MAGE("Mage", "§9", 5, 8, 15, 6, 6, 0.8, 2.0),
    ROGUE("Rogue", "§5", 8, 15, 6, 6, 10, 0.9, 1.0),
    ARCHER("Archer", "§a", 10, 12, 6, 8, 9, 1.0, 1.0);

    private final String displayName;
    private final String color;
    private final int baseStr;
    private final int baseDex;
    private final int baseInt;
    private final int baseVit;
    private final int baseLck;
    private final double healthMultiplier;
    private final double manaMultiplier;

    PlayerClass(String displayName, String color, int baseStr, int baseDex, int baseInt,
                int baseVit, int baseLck, double healthMultiplier, double manaMultiplier) {
        this.displayName = displayName;
        this.color = color;
        this.baseStr = baseStr;
        this.baseDex = baseDex;
        this.baseInt = baseInt;
        this.baseVit = baseVit;
        this.baseLck = baseLck;
        this.healthMultiplier = healthMultiplier;
        this.manaMultiplier = manaMultiplier;
    }

    public String getDisplayName() {
        return color + displayName;
    }

    public String getRawDisplayName() {
        return displayName;
    }

    public int getBaseStr() {
        return baseStr;
    }

    public int getBaseDex() {
        return baseDex;
    }

    public int getBaseInt() {
        return baseInt;
    }

    public int getBaseVit() {
        return baseVit;
    }

    public int getBaseLck() {
        return baseLck;
    }

    public double getHealthMultiplier() {
        return healthMultiplier;
    }

    public double getManaMultiplier() {
        return manaMultiplier;
    }

    public int getBaseStat(StatType stat) {
        return switch (stat) {
            case STR -> baseStr;
            case DEX -> baseDex;
            case INT -> baseInt;
            case VIT -> baseVit;
            case LCK -> baseLck;
        };
    }

    public static PlayerClass fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
