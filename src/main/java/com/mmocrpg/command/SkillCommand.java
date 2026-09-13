package com.mmocrpg.command;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.skill.Skill;
import com.mmocrpg.skill.SkillManager;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillCommand implements CommandExecutor, TabCompleter {
    private final MMORpg plugin;
    private final PlayerManager playerManager;
    private final SkillManager skillManager;

    public SkillCommand(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.skillManager = plugin.getSkillManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null) return true;

        if (args.length == 0) {
            player.sendMessage(ColorUtils.translate("&eUsage: /skill <list|info|learn|bind|use>"));
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("list")) {
            player.sendMessage(ColorUtils.translate("&e&l=== Your Skills ==="));
            for (String skillId : profile.getUnlockedSkills()) {
                skillManager.getSkill(skillId).ifPresent(skill ->
                        player.sendMessage(ColorUtils.translate("&f- " + skill.getName() + " &7(Lv. " + profile.getSkillLevel(skillId) + ") - " + skill.getDescription()))
                );
            }
            return true;
        }

        if (sub.equals("info")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /skill info <skill>"));
                return true;
            }
            skillManager.getSkill(args[1]).ifPresent(skill -> {
                player.sendMessage(ColorUtils.translate("&e" + skill.getName() + " &7(Mana: " + skill.getManaCost() + ", CD: " + skill.getCooldown() + "s, Req: " + skill.getRequiredLevel() + ")"));
                player.sendMessage(ColorUtils.translate("&7" + skill.getDescription()));
            });
            return true;
        }

        if (sub.equals("learn")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /skill learn <skill>"));
                return true;
            }
            skillManager.getSkill(args[1]).ifPresent(skill -> {
                if (profile.getSkillPoints() < 1) {
                    player.sendMessage(ColorUtils.translate("&cNot enough skill points!"));
                    return;
                }
                if (profile.getLevel() < skill.getRequiredLevel()) {
                    player.sendMessage(ColorUtils.translate("&cRequired level: " + skill.getRequiredLevel()));
                    return;
                }
                if (profile.hasUnlockedSkill(skill.getId())) {
                    player.sendMessage(ColorUtils.translate("&cAlready unlocked!"));
                    return;
                }
                profile.setSkillPoints(profile.getSkillPoints() - 1);
                profile.unlockSkill(skill.getId());
                profile.setSkillLevel(skill.getId(), 1);
                player.sendMessage(ColorUtils.translate("&aLearned &e" + skill.getName() + "&a!"));
            });
            return true;
        }

        if (sub.equals("use")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /skill use <skill>"));
                return true;
            }
            skillManager.getSkill(args[1]).ifPresent(skill -> plugin.getSkillExecutor().castSkill(player, skill));
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("list");
            completions.add("info");
            completions.add("learn");
            completions.add("use");
        } else if (args.length >= 2) {
            skillManager.getAllSkills().forEach(s -> completions.add(s.getId()));
        }
        return completions;
    }
}