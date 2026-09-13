package com.mmocrpg.skill.impl;

import com.mmocrpg.skill.Skill;
import com.mmocrpg.util.ParticleUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class RogueSkills {

    public static class Backstab implements Skill {
        @Override public String getId() { return "backstab"; }
        @Override public String getName() { return "Backstab"; }
        @Override public String getDescription() { return "Strike from the shadows for critical damage."; }
        @Override public int getManaCost() { return 20; }
        @Override public double getCooldown() { return 4.0; }
        @Override public int getRequiredLevel() { return 1; }
        @Override public List<String> getPrerequisites() { return List.of(); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.5f);
            ParticleUtils.playCrit(player.getLocation());
            for (Entity entity : player.getNearbyEntities(2.5, 2.5, 2.5)) {
                if (entity instanceof LivingEntity && entity != player) {
                    ((LivingEntity) entity).damage(18.0, player);
                }
            }
        }
    }

    public static class Stealth implements Skill {
        @Override public String getId() { return "stealth"; }
        @Override public String getName() { return "Stealth"; }
        @Override public String getDescription() { return "Become invisible and gain a speed boost."; }
        @Override public int getManaCost() { return 35; }
        @Override public double getCooldown() { return 15.0; }
        @Override public int getRequiredLevel() { return 5; }
        @Override public List<String> getPrerequisites() { return List.of("backstab"); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 0.1f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
        }
    }
}
