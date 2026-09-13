package com.mmocrpg.command;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.quest.Quest;
import com.mmocrpg.quest.QuestManager;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestCommand implements CommandExecutor, TabCompleter {
    private final MMORpg plugin;
    private final PlayerManager playerManager;
    private final QuestManager questManager;

    public QuestCommand(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.questManager = plugin.getQuestManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null) return true;

        if (args.length == 0) {
            player.sendMessage(ColorUtils.translate("&eUsage: /quest <list|accept|abandon|info>"));
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("list")) {
            player.sendMessage(ColorUtils.translate("&e&l=== Available Quests ==="));
            questManager.getQuestIds().forEach(id -> {
                Quest quest = questManager.getQuest(id);
                if (quest != null) {
                    String status = profile.getCompletedQuests().contains(id) ? "&a[Done] " :
                            profile.getActiveQuests().contains(id) ? "&e[Active] " : "&7[Available] ";
                    player.sendMessage(ColorUtils.translate(status + quest.getName() + " &7- " + quest.getDescription()));
                }
            });
            return true;
        }

        if (sub.equals("accept")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /quest accept <quest>"));
                return true;
            }
            questManager.acceptQuest(player, args[1]);
            return true;
        }

        if (sub.equals("abandon")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /quest abandon <quest>"));
                return true;
            }
            profile.abandonQuest(args[1]);
            player.sendMessage(ColorUtils.translate("&7Abandoned quest."));
            return true;
        }

        if (sub.equals("info")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /quest info <quest>"));
                return true;
            }
            questManager.getQuest(args[1]).ifPresent(quest -> {
                player.sendMessage(ColorUtils.translate("&e" + quest.getName()));
                player.sendMessage(ColorUtils.translate("&7" + quest.getDescription()));
                quest.getObjectives().forEach(obj ->
                        player.sendMessage(ColorUtils.translate("&7- " + obj.getDescription()))
                );
            });
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("list");
            completions.add("accept");
            completions.add("abandon");
            completions.add("info");
        } else if (args.length >= 2) {
            questManager.getQuestIds().forEach(completions::add);
        }
        return completions;
    }
}