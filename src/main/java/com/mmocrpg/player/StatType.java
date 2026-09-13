package com.mmocrpg.player;

public enum StatType {

    STR("Strength", "§c"),
    DEX("Dexterity", "§a"),
    INT("Intelligence", "§9"),
    VIT("Vitality", "§6"),
    LCK("Luck", "§e");

    private final String displayName;
    private final String color;

    StatType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    public static StatType fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
