package com.mmocrpg.command;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.player.StatType;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand implements CommandExecutor, TabCompleter {
    private final MMORpg plugin;
    private final PlayerManager playerManager;

    public StatsCommand(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length >= 1) {
            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ColorUtils.translate("&cPlayer not found!"));
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            sender.sendMessage("§cUsage: /stats [player]");
            return true;
        }

        PlayerProfile profile = playerManager.getProfile(target.getUniqueId());
        if (profile == null) return true;

        sender.sendMessage(ColorUtils.translate("&#a0a0ff&l===== " + target.getName() + " Stats ====="));
        sender.sendMessage(ColorUtils.translate("&eLevel: &f" + profile.getLevel()));
        sender.sendMessage(ColorUtils.translate("&eXP: &f" + profile.getExperience() + "/" + plugin.getExperienceManager().getLevelCurve().getXpToNextLevel(profile.getLevel())));
        sender.sendMessage(ColorUtils.translate("&eClass: &f" + (profile.getPlayerClass() != null ? profile.getPlayerClass().getDisplayName() : "&cNone")));

        for (StatType stat : StatType.values()) {
            int base = profile.getBaseStats().getOrDefault(stat, 0);
            int bonus = profile.getBonusStats().getOrDefault(stat, 0);
            sender.sendMessage(ColorUtils.translate(stat.getColor() + stat.getDisplayName() + ": &f" + base + " &8(+" + bonus + ")"));
        }

        sender.sendMessage(ColorUtils.translate("&eHP: &f" + profile.getCurrentHealth() + "&7/&f" + profile.getMaxHealth()));
        sender.sendMessage(ColorUtils.translate("&eMP: &f" + profile.getMana() + "&7/&f" + profile.getMaxMana()));
        sender.sendMessage(ColorUtils.translate("&eSP: &f" + profile.getSkillPoints()));
        sender.sendMessage(ColorUtils.translate("&eStat Points: &f" + profile.getStatPoints()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            plugin.getServer().getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }
        return completions;
    }
}