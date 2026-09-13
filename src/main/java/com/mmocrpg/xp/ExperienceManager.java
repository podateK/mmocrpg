package com.mmocrpg.xp;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ExperienceManager {

    private final MMORpg plugin;
    private final LevelCurve levelCurve;

    public ExperienceManager(MMORpg plugin) {
        this.plugin = plugin;
        this.levelCurve = new LevelCurve(plugin);
    }

    public void addExperience(Player player, long amount, String source) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null || !profile.hasClass()) return;

        profile.setExperience(profile.getExperience() + amount);
        int oldLevel = profile.getLevel();
        int newLevel = levelCurve.getLevelForXp(profile.getExperience());

        if (newLevel > oldLevel) {
            profile.setLevel(newLevel);
            int statPoints = plugin.getConfig().getInt("player.stat-points-per-level", 3);
            int skillPoints = plugin.getConfig().getInt("player.skill-points-per-level", 1);
            profile.setStatPoints(profile.getStatPoints() + statPoints);
            profile.setSkillPoints(profile.getSkillPoints() + skillPoints);
            profile.recalculateStats();
            player.setMaxHealth(profile.getMaxHealth());
            player.setHealth(Math.min(player.getHealth(), profile.getMaxHealth()));

            player.sendActionBar(ColorUtils.component(
                    "&6&lLEVEL UP! &eYou are now level &f" + newLevel + "&e! +" +
                            statPoints + " STAT, +" + skillPoints + " SP"
            ));
        }
    }

    public LevelCurve getLevelCurve() {
        return levelCurve;
    }
}
