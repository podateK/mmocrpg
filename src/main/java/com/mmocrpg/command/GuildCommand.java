package com.mmocrpg.command;

import com.mmocrpg.MMORpg;
import com.mmocrpg.guild.GuildManager;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GuildCommand implements CommandExecutor, TabCompleter {
    private final MMORpg plugin;
    private final PlayerManager playerManager;
    private final GuildManager guildManager;

    public GuildCommand(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.guildManager = plugin.getGuildManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null) return true;

        if (args.length == 0) {
            player.sendMessage(ColorUtils.translate("&eUsage: /guild <create|join|leave|info|invite|kick|list>"));
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("create")) {
            if (args.length < 3) {
                player.sendMessage(ColorUtils.translate("&cUsage: /guild create <name> <tag>"));
                return true;
            }
            guildManager.createGuild(player, args[1], args[2]);
            return true;
        }

        if (sub.equals("join")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /guild join <guild_id>"));
                return true;
            }
            guildManager.join(player, args[1]);
            return true;
        }

        if (sub.equals("leave")) {
            guildManager.leave(player);
            return true;
        }

        if (sub.equals("invite")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /guild invite <player>"));
                return true;
            }
            guildManager.invite(player, args[1]);
            return true;
        }

        if (sub.equals("info")) {
            guildManager.getGuildByPlayer(player.getUniqueId()).ifPresent(guild -> {
                player.sendMessage(ColorUtils.translate("&e" + guild.getName() + " &7[" + guild.getTag() + "]"));
                player.sendMessage(ColorUtils.translate("&7Members: " + guild.getMemberCount() + "/" + plugin.getConfig().getInt("guild.max-members", 50)));
            });
            return true;
        }

        if (sub.equals("list")) {
            player.sendMessage(ColorUtils.translate("&e&l=== Guilds ==="));
            guildManager.getAllGuilds().forEach(g -> player.sendMessage(ColorUtils.translate("&f- " + g.getName() + " &7[" + g.getTag() + "] &7(" + g.getMemberCount() + " members)")));
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("create");
            completions.add("join");
            completions.add("leave");
            completions.add("info");
            completions.add("invite");
            completions.add("list");
        }
        return completions;
    }
}