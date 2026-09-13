package com.mmocrpg.command;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerClass;
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

public class ClassCommand implements CommandExecutor, TabCompleter {
    private final MMORpg plugin;
    private final PlayerManager playerManager;

    public ClassCommand(MMORpg plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        PlayerProfile profile = playerManager.getProfile(player.getUniqueId());
        if (profile == null) return true;

        if (args.length == 0) {
            if (profile.getPlayerClass() == null) {
                player.sendMessage(ColorUtils.translate("&cUsage: /class <class|info>"));
                player.sendMessage(ColorUtils.translate("&eAvailable classes: &fWarrior, Mage, Rogue, Archer"));
            } else {
                player.sendMessage(ColorUtils.translate("&eYour class: &f" + profile.getPlayerClass().getDisplayName()));
            }
            return true;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("info")) {
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translate("&cUsage: /class info <class>"));
                return true;
            }
            PlayerClass clazz = PlayerClass.fromName(args[1]);
            if (clazz == null) {
                player.sendMessage(ColorUtils.translate("&cInvalid class!"));
                return true;
            }
            player.sendMessage(ColorUtils.translate("&e" + clazz.getDisplayName() + " &7- &fBase Stats: STR " + clazz.getBaseStr() + " DEX " + clazz.getBaseDex() + " INT " + clazz.getBaseInt() + " VIT " + clazz.getBaseVit() + " LCK " + clazz.getBaseLck()));
            return true;
        }

        PlayerClass selectedClass = PlayerClass.fromName(sub);
        if (selectedClass == null) {
            player.sendMessage(ColorUtils.translate("&cInvalid class! Use Warrior, Mage, Rogue, or Archer"));
            return true;
        }

        if (profile.hasClass()) {
            if (profile.getLevel() < 5) {
                player.sendMessage(ColorUtils.translate("&cYou can change class at level 5!"));
                return true;
            }
        }

        profile.initializeClass(selectedClass);
        player.sendMessage(ColorUtils.translate("&aYou have chosen the &e" + selectedClass.getDisplayName() + " &aclass!"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("Warrior");
            completions.add("Mage");
            completions.add("Rogue");
            completions.add("Archer");
            completions.add("info");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            completions.add("Warrior");
            completions.add("Mage");
            completions.add("Rogue");
            completions.add("Archer");
        }
        return completions;
    }
}