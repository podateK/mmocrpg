package com.mmocrpg.skill;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillExecutor {
    private final MMORpg plugin;
    private final PlayerManager playerManager;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public SkillExecutor(MMORpg plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    public boolean castSkill(Player player, Skill skill) {
        UUID uuid = player.getUniqueId();
        PlayerProfile profile = playerManager.getProfile(uuid);

        if (!profile.getUnlockedSkills().contains(skill.getId().toLowerCase())) {
            player.sendMessage(ColorUtils.translate("&cYou have not unlocked this skill!"));
            return false;
        }

        if (profile.getLevel() < skill.getRequiredLevel()) {
            player.sendMessage(ColorUtils.translate("&cRequired level: " + skill.getRequiredLevel()));
            return false;
        }

        if (profile.getMana() < skill.getManaCost()) {
            player.sendMessage(ColorUtils.translate("&cNot enough mana!"));
            return false;
        }

        long now = System.currentTimeMillis();
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(uuid, k -> new HashMap<>());
        long cooldownEnd = playerCooldowns.getOrDefault(skill.getId(), 0L);

        if (now < cooldownEnd) {
            double remaining = (cooldownEnd - now) / 1000.0;
            player.sendMessage(ColorUtils.translate(String.format("&cSkill on cooldown for %.1f seconds!", remaining)));
            return false;
        }

        profile.setMana(profile.getMana() - skill.getManaCost());
        playerCooldowns.put(skill.getId(), now + (long) (skill.getCooldown() * 1000.0));

        skill.execute(player);
        return true;
    }
}
