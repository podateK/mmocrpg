package com.mmocrpg.hud;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarManager {
    private final MMORpg plugin;
    private final PlayerManager playerManager;

    public ActionBarManager(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    public void update(Player player) {
        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null || !plugin.getConfig().getBoolean("hud.action-bar-enabled", true)) return;

        String msg;
        if (!profile.hasClass()) {
            msg = ColorUtils.translate("&cChoose a class with &f/class <class>");
        } else {
            msg = ColorUtils.translate("&eLevel &f" + profile.getLevel() + " &e| &cHP &f" + profile.getCurrentHealth() + "/" + profile.getMaxHealth() + " &e| &9MP &f" + profile.getMana() + "/" + profile.getMaxMana() + " &e| &aSP &f" + profile.getSkillPoints());
        }
        player.sendActionBar(ColorUtils.component(msg));
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
}