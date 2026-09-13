package com.mmocrpg.combat;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DamageCalculator {
    private final MMORpg plugin;
    private final Random random = new Random();

    public DamageCalculator(MMORpg plugin) {
        this.plugin = plugin;
    }

    public double calculateDamage(PlayerProfile attacker, LivingEntity target, double baseDamage) {
        double damage = baseDamage;
        damage += attacker.getStat(com.mmocrpg.player.StatType.STR) * plugin.getConfig().getDouble("combat.damage-per-str", 0.5);
        damage *= (1.0 + attacker.getBonusStats().getOrDefault(com.mmocrpg.player.StatType.STR, 0) * 0.01);

        if (isCriticalHit(attacker)) {
            damage *= plugin.getConfig().getDouble("combat.crit-multiplier", 1.5);
        }

        damage = applyDefense(damage, target);
        damage += random.nextGaussian() * damage * 0.05;
        return Math.max(1.0, damage);
    }

    public double calculateSkillDamage(PlayerProfile attacker, LivingEntity target, double skillDamage) {
        double damage = skillDamage;
        double intScaling = attacker.getStat(com.mmocrpg.player.StatType.INT) * 0.3;
        damage += intScaling;

        if (isCriticalHit(attacker)) {
            damage *= plugin.getConfig().getDouble("combat.crit-multiplier", 1.5);
        }

        return Math.max(1.0, damage);
    }

    private boolean isCriticalHit(PlayerProfile attacker) {
        double baseChance = plugin.getConfig().getDouble("combat.crit-base-chance", 0.05);
        double lckChance = attacker.getStat(com.mmocrpg.player.StatType.LCK) * plugin.getConfig().getDouble("combat.crit-per-lck", 0.005);
        return random.nextDouble() < (baseChance + lckChance);
    }

    private double applyDefense(double damage, LivingEntity target) {
        double defense = target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR).getValue() * plugin.getConfig().getDouble("combat.armor-toughness", 0.04);
        double defenseValue = target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR_TOUGHNESS).getValue() * 0.5;
        double reduction = defense / (defense + 100.0) + defenseValue * 0.01;
        return damage * (1.0 - Math.min(0.8, reduction));
    }

    public boolean canDodge(PlayerProfile defender) {
        double baseChance = plugin.getConfig().getDouble("combat.dodge-base-chance", 0.02);
        double dexChance = defender.getStat(com.mmocrpg.player.StatType.DEX) * plugin.getConfig().getDouble("combat.dodge-per-dex", 0.004);
        return random.nextDouble() < (baseChance + dexChance);
    }

    public boolean canBlock(PlayerProfile defender) {
        double baseChance = plugin.getConfig().getDouble("combat.block-base-chance", 0.03);
        return random.nextDouble() < baseChance;
    }
}