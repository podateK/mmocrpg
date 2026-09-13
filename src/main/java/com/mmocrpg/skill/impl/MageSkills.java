package com.mmocrpg.skill.impl;

import com.mmocrpg.skill.Skill;
import com.mmocrpg.util.ParticleUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class MageSkills {

    public static class Fireball implements Skill {
        @Override public String getId() { return "fireball"; }
        @Override public String getName() { return "Fireball"; }
        @Override public String getDescription() { return "Launch a fiery projectile that explodes on impact."; }
        @Override public int getManaCost() { return 30; }
        @Override public double getCooldown() { return 3.0; }
        @Override public int getRequiredLevel() { return 1; }
        @Override public List<String> getPrerequisites() { return List.of(); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
            ParticleUtils.playFlame(player.getLocation().add(0, 1.5, 0));
            for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
                if (entity instanceof LivingEntity && entity != player) {
                    ((LivingEntity) entity).damage(15.0, player);
                }
            }
        }
    }

    public static class Blizzard implements Skill {
        @Override public String getId() { return "blizzard"; }
        @Override public String getName() { return "Blizzard"; }
        @Override public String getDescription() { return "Call down ice storms to freeze enemies."; }
        @Override public int getManaCost() { return 50; }
        @Override public double getCooldown() { return 12.0; }
        @Override public int getRequiredLevel() { return 6; }
        @Override public List<String> getPrerequisites() { return List.of("fireball"); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 0.5f);
            ParticleUtils.playMagic(player.getLocation());
            player.sendMessage("You conjure a freezing blizzard!");
        }
    }
}
