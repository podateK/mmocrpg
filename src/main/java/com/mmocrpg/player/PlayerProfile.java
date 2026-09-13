package com.mmocrpg.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private String name;
    private PlayerClass playerClass;
    private int level;
    private long experience;
    private int skillPoints;
    private int statPoints;
    private final Map<StatType, Integer> baseStats;
    private final Map<StatType, Integer> bonusStats;
    private final Map<String, Integer> skillLevels;
    private final Set<String> unlockedSkills;
    private final Set<String> activeQuests;
    private final Set<String> completedQuests;
    private final Map<String, Integer> questProgress;
    private int maxHealth;
    private int currentHealth;
    private int maxMana;
    private int currentMana;
    private String guildId;
    private long lastLogin;
    private long totalPlaytime;

    public PlayerProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.playerClass = null;
        this.level = 1;
        this.experience = 0;
        this.skillPoints = 0;
        this.statPoints = 0;
        this.baseStats = new HashMap<>();
        this.bonusStats = new HashMap<>();
        this.skillLevels = new HashMap<>();
        this.unlockedSkills = new HashSet<>();
        this.activeQuests = new HashSet<>();
        this.completedQuests = new HashSet<>();
        this.questProgress = new HashMap<>();
        this.maxHealth = 20;
        this.currentHealth = 20;
        this.maxMana = 50;
        this.currentMana = 50;
        this.guildId = null;
        this.lastLogin = System.currentTimeMillis();
        this.totalPlaytime = 0;

        for (StatType stat : StatType.values()) {
            baseStats.put(stat, 0);
            bonusStats.put(stat, 0);
        }
    }

    public void initializeClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
        for (StatType stat : StatType.values()) {
            baseStats.put(stat, playerClass.getBaseStat(stat));
        }
        recalculateStats();
    }

    public void recalculateStats() {
        bonusStats.clear();
        int vit = getStat(StatType.VIT);
        int intel = getStat(StatType.INT);
        maxHealth = (int) (20 + vit * 2 * (playerClass != null ? playerClass.getHealthMultiplier() : 1.0));
        maxMana = (int) (50 + intel * 3 * (playerClass != null ? playerClass.getManaMultiplier() : 1.0));
        currentHealth = Math.min(currentHealth, maxHealth);
        currentMana = Math.min(currentMana, maxMana);
    }

    public int getStat(StatType stat) {
        return baseStats.getOrDefault(stat, 0) + bonusStats.getOrDefault(stat, 0);
    }

    public void addBaseStat(StatType stat, int amount) {
        baseStats.merge(stat, amount, Integer::sum);
        recalculateStats();
    }

    public void addBonusStat(StatType stat, int amount) {
        bonusStats.merge(stat, amount, Integer::sum);
    }

    public boolean hasSkillPoints(int amount) {
        return skillPoints >= amount;
    }

    public void spendSkillPoints(int amount) {
        skillPoints -= amount;
    }

    public boolean hasStatPoints(int amount) {
        return statPoints >= amount;
    }

    public void spendStatPoints(int amount) {
        statPoints -= amount;
    }

    public int getSkillLevel(String skillId) {
        return skillLevels.getOrDefault(skillId, 0);
    }

    public void setSkillLevel(String skillId, int level) {
        skillLevels.put(skillId, level);
    }

    public boolean hasUnlockedSkill(String skillId) {
        return unlockedSkills.contains(skillId);
    }

    public void unlockSkill(String skillId) {
        unlockedSkills.add(skillId);
    }

    public void addQuest(String questId) {
        activeQuests.add(questId);
    }

    public void completeQuest(String questId) {
        activeQuests.remove(questId);
        completedQuests.add(questId);
    }

    public void abandonQuest(String questId) {
        activeQuests.remove(questId);
        questProgress.remove(questId);
    }

    public int getQuestProgress(String questId) {
        return questProgress.getOrDefault(questId, 0);
    }

    public void setQuestProgress(String questId, int progress) {
        questProgress.put(questId, progress);
    }

    public void incrementQuestProgress(String questId, int amount) {
        questProgress.merge(questId, amount, Integer::sum);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }

    public Map<StatType, Integer> getBaseStats() {
        return baseStats;
    }

    public Map<StatType, Integer> getBonusStats() {
        return bonusStats;
    }

    public Map<String, Integer> getSkillLevels() {
        return skillLevels;
    }

    public Set<String> getUnlockedSkills() {
        return unlockedSkills;
    }

    public Set<String> getActiveQuests() {
        return activeQuests;
    }

    public Set<String> getCompletedQuests() {
        return completedQuests;
    }

    public Map<String, Integer> getQuestProgress() {
        return questProgress;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.max(0, Math.min(currentHealth, maxHealth));
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.max(0, Math.min(currentMana, maxMana));
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMana() {
        return currentMana;
    }

    public void setMana(int mana) {
        this.currentMana = Math.max(0, Math.min(mana, maxMana));
    }

    public int getMaxMana() {
        return maxMana;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(long totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    public boolean hasClass() {
        return playerClass != null;
    }

    public void takeDamage(int amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }

    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public void useMana(int amount) {
        currentMana = Math.max(0, currentMana - amount);
    }

    public void restoreMana(int amount) {
        currentMana = Math.min(maxMana, currentMana + amount);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }
}
