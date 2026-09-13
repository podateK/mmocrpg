package com.mmocrpg.combat;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatManager {
    private final MMORpg plugin;
    private final DamageCalculator damageCalculator;

    public CombatManager(MMORpg plugin) {
        this.plugin = plugin;
        this.damageCalculator = new DamageCalculator(plugin);
    }

    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null || !profile.hasClass()) return;

        double baseDamage = plugin.getConfig().getDouble("combat.base-damage", 1.0);
        double damage = damageCalculator.calculateDamage(profile, target, baseDamage);

        if (damageCalculator.canDodge(profile)) {
            event.setCancelled(true);
            player.sendActionBar(ColorUtils.component("&aYou dodged the attack!"));
            return;
        }

        if (damageCalculator.canBlock(profile)) {
            double blocked = damage * 0.5;
            damage -= blocked;
            player.sendActionBar(ColorUtils.component("&aBlocked! Reduced damage by " + (int)blocked));
        }

        event.setDamage(damage);
    }

    public double calculateSkillDamage(PlayerProfile profile, LivingEntity target, double skillBaseDamage) {
        return damageCalculator.calculateSkillDamage(profile, target, skillBaseDamage);
    }

    public DamageCalculator getDamageCalculator() {
        return damageCalculator;
    }
}