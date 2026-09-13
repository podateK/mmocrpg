package com.mmocrpg.listener;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final MMORpg plugin;
    private final PlayerManager playerManager;

    public PlayerListener(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = playerManager.getOrCreateProfile(player.getUniqueId(), player.getName());
        profile.setLastLogin(System.currentTimeMillis());

        if (profile.hasClass()) {
            player.setMaxHealth(profile.getMaxHealth());
            player.setHealth(Math.min(player.getHealth(), profile.getMaxHealth()));
        }

        plugin.getActionBarManager().update(player);
        plugin.getScoreboardManager().update(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile != null) {
            long playtime = System.currentTimeMillis() - profile.getLastLogin();
            profile.setTotalPlaytime(profile.getTotalPlaytime() + playtime);
            playerManager.saveProfile(profile);
        }
        plugin.getScoreboardManager().remove(player);
    }
}