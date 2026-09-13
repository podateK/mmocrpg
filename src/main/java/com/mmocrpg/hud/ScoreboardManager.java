package com.mmocrpg.hud;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardManager {
    private final MMORpg plugin;
    private final PlayerManager playerManager;

    public ScoreboardManager(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    public void update(Player player) {
        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null || !plugin.getConfig().getBoolean("hud.scoreboard-enabled", true)) return;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("mmocrpg", "dummy", ColorUtils.component("&#a0a0ff&lMMORpg"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (profile.hasClass()) {
            objective.getScore(ColorUtils.component("&eLevel: &f" + profile.getLevel())).setScore(8);
            objective.getScore(ColorUtils.component("&eClass: &f" + profile.getPlayerClass().getDisplayName())).setScore(7);
            objective.getScore(ColorUtils.component("&cHP: &f" + profile.getCurrentHealth() + "/" + profile.getMaxHealth())).setScore(6);
            objective.getScore(ColorUtils.component("&9MP: &f" + profile.getMana() + "/" + profile.getMaxMana())).setScore(5);
            objective.getScore(ColorUtils.component("&aSP: &f" + profile.getSkillPoints())).setScore(4);
            objective.getScore(ColorUtils.component("&eStr: &f" + profile.getStat(com.mmocrpg.player.StatType.STR))).setScore(3);
            objective.getScore(ColorUtils.component("&eDex: &f" + profile.getStat(com.mmocrpg.player.StatType.DEX))).setScore(2);
            objective.getScore(ColorUtils.component("&eInt: &f" + profile.getStat(com.mmocrpg.player.StatType.INT))).setScore(1);
        } else {
            objective.getScore(ColorUtils.component("&cNo class selected")).setScore(8);
            objective.getScore(ColorUtils.component("&eUse &f/class <class>")).setScore(7);
        }

        player.setScoreboard(scoreboard);
    }

    public void remove(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    update(player);
                }
            }
        }.runTaskTimer(plugin, 0, plugin.getConfig().getLong("hud.update-interval", 20));
    }

    public void shutdown() {
    }
}