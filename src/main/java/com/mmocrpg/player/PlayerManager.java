package com.mmocrpg.player;

import com.mmocrpg.MMORpg;
import com.mmocrpg.util.ColorUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class PlayerManager {

    private final MMORpg plugin;
    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();
    private final File dataFolder;

    public PlayerManager(MMORpg plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public PlayerProfile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public PlayerProfile getOrCreateProfile(UUID uuid, String name) {
        return profiles.computeIfAbsent(uuid, id -> {
            PlayerProfile profile = new PlayerProfile(id, name);
            loadProfile(profile);
            return profile;
        });
    }

    public void loadProfile(PlayerProfile profile) {
        File file = getProfileFile(profile.getUuid());
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        profile.setLevel(config.getInt("level", 1));
        profile.setExperience(config.getLong("experience", 0));
        profile.setSkillPoints(config.getInt("skill-points", 0));
        profile.setStatPoints(config.getInt("stat-points", 0));
        profile.setLastLogin(config.getLong("last-login", System.currentTimeMillis()));
        profile.setTotalPlaytime(config.getLong("total-playtime", 0));
        profile.setGuildId(config.getString("guild-id", null));

        String className = config.getString("class", null);
        if (className != null) {
            PlayerClass playerClass = PlayerClass.fromName(className);
            if (playerClass != null) {
                profile.initializeClass(playerClass);
            }
        }

        if (config.contains("base-stats")) {
            for (String key : config.getConfigurationSection("base-stats").getKeys(false)) {
                StatType stat = StatType.fromName(key);
                if (stat != null) {
                    profile.getBaseStats().put(stat, config.getInt("base-stats." + key));
                }
            }
        }

        if (config.contains("skill-levels")) {
            for (String key : config.getConfigurationSection("skill-levels").getKeys(false)) {
                profile.setSkillLevel(key, config.getInt("skill-levels." + key));
            }
        }

        if (config.contains("unlocked-skills")) {
            for (String skill : config.getStringList("unlocked-skills")) {
                profile.unlockSkill(skill);
            }
        }

        profile.setCurrentHealth(config.getInt("health", profile.getMaxHealth()));
        profile.setCurrentMana(config.getInt("mana", profile.getMaxMana()));
        profile.recalculateStats();
    }

    public void saveProfile(PlayerProfile profile) {
        File file = getProfileFile(profile.getUuid());
        FileConfiguration config = new YamlConfiguration();

        config.set("name", profile.getName());
        config.set("level", profile.getLevel());
        config.set("experience", profile.getExperience());
        config.set("skill-points", profile.getSkillPoints());
        config.set("stat-points", profile.getStatPoints());
        config.set("last-login", profile.getLastLogin());
        config.set("total-playtime", profile.getTotalPlaytime());
        config.set("guild-id", profile.getGuildId());
        config.set("health", profile.getCurrentHealth());
        config.set("mana", profile.getCurrentMana());

        if (profile.getPlayerClass() != null) {
            config.set("class", profile.getPlayerClass().name());
        }

        for (Map.Entry<StatType, Integer> entry : profile.getBaseStats().entrySet()) {
            config.set("base-stats." + entry.getKey().name(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : profile.getSkillLevels().entrySet()) {
            config.set("skill-levels." + entry.getKey(), entry.getValue());
        }

        config.set("unlocked-skills", profile.getUnlockedSkills().stream().toList());
        config.set("active-quests", profile.getActiveQuests().stream().toList());
        config.set("completed-quests", profile.getCompletedQuests().stream().toList());

        for (Map.Entry<String, Integer> entry : profile.getQuestProgress().entrySet()) {
            config.set("quest-progress." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save profile for " + profile.getUuid(), e);
        }
    }

    public void saveAll() {
        for (PlayerProfile profile : profiles.values()) {
            saveProfile(profile);
        }
    }

    public void removeProfile(UUID uuid) {
        profiles.remove(uuid);
    }

    private File getProfileFile(UUID uuid) {
        return new File(dataFolder, uuid.toString() + ".yml");
    }
}
