package com.mmocrpg.guild;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GuildManager {
    private final MMORpg plugin;
    private final Map<String, Guild> guilds = new HashMap<>();
    private final Map<UUID, String> playerGuild = new HashMap<>();

    public GuildManager(MMORpg plugin) {
        this.plugin = plugin;
    }

    public void createGuild(Player player, String name, String tag) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null) return;

        if (playerGuild.containsKey(player.getUniqueId())) {
            player.sendMessage(ColorUtils.colorize("&cYou are already in a guild!"));
            return;
        }

        if (profile.getLevel() < plugin.getConfig().getInt("guild.require-level", 5)) {
            player.sendMessage(ColorUtils.colorize("&cYou need level " + plugin.getConfig().getInt("guild.require-level", 5) + " to create a guild!"));
            return;
        }

        String id = name.toLowerCase().replace(" ", "_");
        if (guilds.containsKey(id)) {
            player.sendMessage(ColorUtils.colorize("&cGuild name already taken!"));
            return;
        }

        Guild guild = new Guild(id, name, tag.toUpperCase(), player.getUniqueId());
        guilds.put(id, guild);
        playerGuild.put(player.getUniqueId(), id);
        profile.setGuildId(id);
        player.sendMessage(ColorUtils.colorize("&aGuild &e" + name + " &acreated! Tag: &e[" + tag.toUpperCase() + "]&a"));
    }

    public void invite(Player player, String targetName) {
        Player target = plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ColorUtils.colorize("&cPlayer not found!"));
            return;
        }

        String guildId = playerGuild.get(player.getUniqueId());
        if (guildId == null) {
            player.sendMessage(ColorUtils.colorize("&cYou are not in a guild!"));
            return;
        }

        Guild guild = guilds.get(guildId);
        if (guild == null) return;

        GuildRank rank = guild.getRank(player.getUniqueId());
        if (rank == null || !rank.canInvite()) {
            player.sendMessage(ColorUtils.colorize("&cYou don't have permission to invite!"));
            return;
        }

        if (guild.getMemberCount() >= plugin.getConfig().getInt("guild.max-members", 50)) {
            player.sendMessage(ColorUtils.colorize("&cGuild is full!"));
            return;
        }

        guild.invite(target.getUniqueId());
        target.sendMessage(ColorUtils.colorize("&e" + player.getName() + " &ainvited you to &e" + guild.getName() + "&a. Use /guild join " + guild.getId()));
        player.sendMessage(ColorUtils.colorize("&aInvited &e" + target.getName() + "&a to the guild!"));
    }

    public void join(Player player, String guildId) {
        Guild guild = guilds.get(guildId);
        if (guild == null) {
            player.sendMessage(ColorUtils.colorize("&cGuild not found!"));
            return;
        }

        if (!guild.hasInvite(player.getUniqueId())) {
            player.sendMessage(ColorUtils.colorize("&cNo invite from this guild!"));
            return;
        }

        if (playerGuild.containsKey(player.getUniqueId())) {
            player.sendMessage(ColorUtils.colorize("&cYou are already in a guild!"));
            return;
        }

        guild.acceptInvite(player.getUniqueId());
        playerGuild.put(player.getUniqueId(), guildId);
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile != null) profile.setGuildId(guildId);
        player.sendMessage(ColorUtils.colorize("&aYou joined &e" + guild.getName() + "&a!"));
    }

    public void leave(Player player) {
        String guildId = playerGuild.remove(player.getUniqueId());
        if (guildId == null) {
            player.sendMessage(ColorUtils.colorize("&cYou are not in a guild!"));
            return;
        }

        Guild guild = guilds.get(guildId);
        if (guild != null) {
            if (guild.getOwner().equals(player.getUniqueId())) {
                disband(guildId);
            } else {
                guild.removeMember(player.getUniqueId());
                player.sendMessage(ColorUtils.colorize("&7You left &e" + guild.getName() + "&7."));
            }
        }

        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile != null) profile.setGuildId(null);
    }

    public void disband(String guildId) {
        Guild guild = guilds.remove(guildId);
        if (guild == null) return;

        for (UUID member : guild.getMembers().keySet()) {
            playerGuild.remove(member);
            PlayerProfile profile = plugin.getPlayerManager().getProfile(member);
            if (profile != null) profile.setGuildId(null);
        }

        plugin.getServer().broadcastMessage(ColorUtils.colorize("&e" + guild.getName() + " &ahas been disbanded."));
    }

    public Optional<Guild> getGuild(String id) {
        return Optional.ofNullable(guilds.get(id));
    }

    public Optional<Guild> getGuildByPlayer(UUID uuid) {
        String guildId = playerGuild.get(uuid);
        return guildId != null ? Optional.ofNullable(guilds.get(guildId)) : Optional.empty();
    }

    public java.util.Collection<Guild> getAllGuilds() {
        return guilds.values();
    }

    public void saveAll() {
    }
}