package com.mmocrpg.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Random;

public final class ParticleUtils {

    private static final Random RANDOM = new Random();

    private ParticleUtils() {
    }

    public static void spawnCircle(Location center, Particle particle, double radius, int count) {
        World world = center.getWorld();
        if (world == null) return;
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            world.spawnParticle(particle, x, center.getY(), z, 1, 0, 0, 0, 0);
        }
    }

    public static void spawnHelix(Location center, Particle particle, double radius, double height, int points) {
        World world = center.getWorld();
        if (world == null) return;
        for (int i = 0; i < points; i++) {
            double t = (double) i / points;
            double angle = 4 * Math.PI * t;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + height * t;
            double z = center.getZ() + radius * Math.sin(angle);
            world.spawnParticle(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    public static void spawnLine(Location start, Location end, Particle particle, double spacing) {
        World world = start.getWorld();
        if (world == null) return;
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        direction.normalize();
        for (double d = 0; d < distance; d += spacing) {
            Location point = start.clone().add(direction.clone().multiply(d));
            world.spawnParticle(particle, point, 1, 0, 0, 0, 0);
        }
    }

    public static void spawnExplosion(Location center) {
        World world = center.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.EXPLOSION_EMITTER, center, 1);
        world.spawnParticle(Particle.FLAME, center, 30, 0.5, 0.5, 0.5, 0.05);
        world.spawnParticle(Particle.SMOKE, center, 20, 0.3, 0.3, 0.3, 0.02);
    }

    public static void spawnCritical(Location center) {
        World world = center.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.CRIT, center, 15, 0.3, 0.3, 0.3, 0.1);
        world.spawnParticle(Particle.CRIT_MAGIC, center, 8, 0.2, 0.2, 0.2, 0.15);
    }

    public static void spawnHeal(Location center) {
        World world = center.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.VILLAGER_HAPPY, center, 20, 0.5, 1.0, 0.5, 0);
    }

    public static void spawnManaRestore(Location center) {
        World world = center.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.ENCHANT, center, 15, 0.5, 0.5, 0.5, 0.5);
    }

    public static void spawnShield(Location center, double radius) {
        World world = center.getWorld();
        if (world == null) return;
        for (int i = 0; i < 40; i++) {
            double theta = RANDOM.nextDouble() * Math.PI;
            double phi = RANDOM.nextDouble() * 2 * Math.PI;
            double x = radius * Math.sin(theta) * Math.cos(phi);
            double y = radius * Math.cos(theta);
            double z = radius * Math.sin(theta) * Math.sin(phi);
            world.spawnParticle(Particle.END_ROD, center.clone().add(x, y + 1, z), 1, 0, 0, 0, 0);
        }
    }

    public static void spawnDashTrailing(LivingEntity entity) {
        World world = entity.getWorld();
        if (world == null) return;
        Location loc = entity.getLocation().add(0, 1, 0);
        world.spawnParticle(Particle.CLOUD, loc, 5, 0.1, 0.1, 0.1, 0.02);
        world.spawnParticle(Particle.SMOKE, loc, 3, 0.05, 0.05, 0.05, 0.01);
    }

    public static void spawnFireAura(LivingEntity entity) {
        World world = entity.getWorld();
        if (world == null) return;
        for (int i = 0; i < 8; i++) {
            double angle = 2 * Math.PI * i / 8;
            double x = entity.getLocation().getX() + 0.8 * Math.cos(angle);
            double z = entity.getLocation().getZ() + 0.8 * Math.sin(angle);
            world.spawnParticle(Particle.FLAME, x, entity.getLocation().getY() + 0.5, z, 1, 0, 0.1, 0, 0.01);
        }
    }

    public static void spawnFrostAura(LivingEntity entity) {
        World world = entity.getWorld();
        if (world == null) return;
        for (int i = 0; i < 12; i++) {
            double angle = 2 * Math.PI * i / 12;
            double x = entity.getLocation().getX() + 1.0 * Math.cos(angle);
            double z = entity.getLocation().getZ() + 1.0 * Math.sin(angle);
            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(150, 200, 255), 1.0f);
            world.spawnParticle(Particle.REDSTONE, x, entity.getLocation().getY() + 0.3, z, 1, 0, 0, 0, 0, dust);
        }
    }

    public static void spawnPoisonCloud(LivingEntity entity) {
        World world = entity.getWorld();
        if (world == null) return;
        Location loc = entity.getLocation().add(0, 1, 0);
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 200, 0), 1.0f);
        world.spawnParticle(Particle.REDSTONE, loc, 15, 0.5, 0.5, 0.5, 0, dust);
    }

    public static void playCrit(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.CRIT, location, 15, 0.3, 0.3, 0.3, 0.1);
    }

    public static void playFlame(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.FLAME, location, 20, 0.3, 0.3, 0.3, 0.05);
    }

    public static void playMagic(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.ENCHANT, location, 15, 0.5, 0.5, 0.5, 0.5);
    }

    public static void spawnSlash(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        world.spawnParticle(Particle.SWEEP_ATTACK, location, 5, 1.0, 0.5, 1.0, 0);
    }
}
