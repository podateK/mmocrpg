package com.mmocrpg.skill.impl;

import com.mmocrpg.skill.Skill;
import com.mmocrpg.util.ParticleUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class WarriorSkills {

    public static class PowerStrike implements Skill {
        @Override public String getId() { return "power_strike"; }
        @Override public String getName() { return "Power Strike"; }
        @Override public String getDescription() { return "A devastating melee strike dealing heavy damage."; }
        @Override public int getManaCost() { return 15; }
        @Override public double getCooldown() { return 4.0; }
        @Override public int getRequiredLevel() { return 1; }
        @Override public List<String> getPrerequisites() { return List.of(); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 0.5f);
            ParticleUtils.playCrit(player.getLocation().add(0, 1, 0));
            for (Entity entity : player.getNearbyEntities(3.0, 3.0, 3.0)) {
                if (entity instanceof LivingEntity && entity != player) {
                    ((LivingEntity) entity).damage(12.0, player);
                }
            }
        }
    }

    public static class WarCry implements Skill {
        @Override public String getId() { return "war_cry"; }
        @Override public String getName() { return "War Cry"; }
        @Override public String getDescription() { return "Intimidate nearby enemies and boost defense."; }
        @Override public int getManaCost() { return 25; }
        @Override public double getCooldown() { return 15.0; }
        @Override public int getRequiredLevel() { return 5; }
        @Override public List<String> getPrerequisites() { return List.of("power_strike"); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
            ParticleUtils.playMagic(player.getLocation());
            player.sendMessage("You unleash a fierce war cry!");
        }
    }
}
