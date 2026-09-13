package com.mmocrpg.xp;

import com.mmocrpg.MMORpg;

public final class LevelCurve {

    private final MMORpg plugin;
    private final double baseXp;
    private final double curveMultiplier;
    private final int xpPerLevelBonus;

    public LevelCurve(MMORpg plugin) {
        this.plugin = plugin;
        this.baseXp = plugin.getConfig().getDouble("xp.base-xp", 100);
        this.curveMultiplier = plugin.getConfig().getDouble("xp.curve-multiplier", 1.15);
        this.xpPerLevelBonus = plugin.getConfig().getInt("xp.xp-per-level-bonus", 10);
    }

    public long getXpForLevel(int level) {
        if (level <= 1) return 0;
        return (long) (baseXp * Math.pow(curveMultiplier, level - 2) + xpPerLevelBonus * (level - 1));
    }

    public int getLevelForXp(long totalXp) {
        int level = 1;
        long required = 0;
        while (level < 100) {
            required += getXpForLevel(level + 1);
            if (totalXp < required) break;
            level++;
        }
        return Math.min(level, 100);
    }

    public long getXpProgressInLevel(long totalXp, int currentLevel) {
        long xpForCurrent = 0;
        for (int i = 1; i < currentLevel; i++) {
            xpForCurrent += getXpForLevel(i + 1);
        }
        return totalXp - xpForCurrent;
    }

    public long getXpToNextLevel(int currentLevel) {
        return getXpForLevel(currentLevel + 1);
    }

    public double getLevelProgress(long totalXp, int currentLevel) {
        long progress = getXpProgressInLevel(totalXp, currentLevel);
        long required = getXpToNextLevel(currentLevel);
        return required > 0 ? (double) progress / required : 0;
    }
}
