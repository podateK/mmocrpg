package com.mmocrpg.listener;

import com.mmocrpg.MMORpg;
import com.mmocrpg.combat.CombatManager;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.quest.QuestManager;
import com.mmocrpg.quest.QuestObjectiveType;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class CombatListener implements Listener {
    private final MMORpg plugin;
    private final CombatManager combatManager;
    private final PlayerManager playerManager;
    private final QuestManager questManager;

    public CombatListener(MMORpg plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
        this.playerManager = plugin.getPlayerManager();
        this.questManager = plugin.getQuestManager();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        combatManager.onEntityDamage(event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() instanceof Player killer) {
            PlayerProfile profile = playerManager.getProfile(killer.getUniqueId());
            if (profile != null) {
                double xp = plugin.getConfig().getDouble("xp.xp-per-kill", 50);
                plugin.getExperienceManager().addExperience(killer, (long) xp, "kill");
                questManager.updateProgress(killer, QuestObjectiveType.KILL, entity.getType().name(), 1);
            }
        }
    }
}