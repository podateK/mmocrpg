package com.mmocrpg;

import com.mmocrpg.combat.CombatManager;
import com.mmocrpg.guild.GuildManager;
import com.mmocrpg.hud.ActionBarManager;
import com.mmocrpg.hud.ScoreboardManager;
import com.mmocrpg.listener.CombatListener;
import com.mmocrpg.listener.PlayerListener;
import com.mmocrpg.player.PlayerManager;
import com.mmocrpg.quest.QuestManager;
import com.mmocrpg.skill.SkillExecutor;
import com.mmocrpg.skill.SkillManager;
import com.mmocrpg.xp.ExperienceManager;

import org.bukkit.plugin.java.JavaPlugin;

public final class MMORpg extends JavaPlugin {

    private static MMORpg instance;
    private PlayerManager playerManager;
    private SkillManager skillManager;
    private SkillExecutor skillExecutor;
    private QuestManager questManager;
    private GuildManager guildManager;
    private CombatManager combatManager;
    private ExperienceManager experienceManager;
    private ActionBarManager actionBarManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.playerManager = new PlayerManager(this);
        this.skillManager = new SkillManager();
        this.skillExecutor = new SkillExecutor(this, playerManager);
        this.questManager = new QuestManager(this);
        this.guildManager = new GuildManager(this);
        this.combatManager = new CombatManager(this);
        this.experienceManager = new ExperienceManager(this);
        this.actionBarManager = new ActionBarManager(this);
        this.scoreboardManager = new ScoreboardManager(this);

        getCommand("class").setExecutor(new com.mmocrpg.command.ClassCommand(this));
        getCommand("skill").setExecutor(new com.mmocrpg.command.SkillCommand(this));
        getCommand("quest").setExecutor(new com.mmocrpg.command.QuestCommand(this));
        getCommand("guild").setExecutor(new com.mmocrpg.command.GuildCommand(this));
        getCommand("stats").setExecutor(new com.mmocrpg.command.StatsCommand(this));

        getCommand("class").setTabCompleter(new com.mmocrpg.command.ClassCommand(this));
        getCommand("skill").setTabCompleter(new com.mmocrpg.command.SkillCommand(this));
        getCommand("quest").setTabCompleter(new com.mmocrpg.command.QuestCommand(this));
        getCommand("guild").setTabCompleter(new com.mmocrpg.command.GuildCommand(this));
        getCommand("stats").setTabCompleter(new com.mmocrpg.command.StatsCommand(this));

        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getLogger().info("MMORpg enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (playerManager != null) {
            playerManager.saveAll();
        }
        if (guildManager != null) {
            guildManager.saveAll();
        }
        if (scoreboardManager != null) {
            scoreboardManager.shutdown();
        }
        getLogger().info("MMORpg disabled.");
    }

    public static MMORpg getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SkillExecutor getSkillExecutor() {
        return skillExecutor;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public ExperienceManager getExperienceManager() {
        return experienceManager;
    }

    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
