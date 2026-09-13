package com.mmocrpg.skill.impl;

import com.mmocrpg.skill.Skill;
import com.mmocrpg.util.ParticleUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;

import java.util.List;

public class ArcherSkills {

    public static class MultiShot implements Skill {
        @Override public String getId() { return "multi_shot"; }
        @Override public String getName() { return "Multi Shot"; }
        @Override public String getDescription() { return "Fire multiple arrows in a spread."; }
        @Override public int getManaCost() { return 20; }
        @Override public double getCooldown() { return 6.0; }
        @Override public int getRequiredLevel() { return 1; }
        @Override public List<String> getPrerequisites() { return List.of(); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.2f);
            for (int i = -1; i <= 1; i++) {
                Arrow arrow = player.launchProjectile(Arrow.class);
                arrow.setVelocity(player.getLocation().getDirection().clone().rotateAroundY(Math.toRadians(i * 15)).multiply(2.5));
            }
        }
    }

    public static class PiercingArrow implements Skill {
        @Override public String getId() { return "piercing_arrow"; }
        @Override public String getName() { return "Piercing Arrow"; }
        @Override public String getDescription() { return "An arrow that pierces through multiple enemies."; }
        @Override public int getManaCost() { return 30; }
        @Override public double getCooldown() { return 10.0; }
        @Override public int getRequiredLevel() { return 5; }
        @Override public List<String> getPrerequisites() { return List.of("multi_shot"); }

        @Override
        public void execute(Player player) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 0.8f);
            Arrow arrow = player.launchProjectile(Arrow.class);
            arrow.setVelocity(player.getLocation().getDirection().multiply(3.0));
            arrow.setPierceLevel((byte) 5);
        }
    }
}