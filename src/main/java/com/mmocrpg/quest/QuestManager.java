package com.mmocrpg.quest;

import com.mmocrpg.MMORpg;
import com.mmocrpg.player.PlayerProfile;
import com.mmocrpg.util.ColorUtils;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QuestManager {
    private final MMORpg plugin;
    private final Map<String, Quest> quests = new HashMap<>();

    public QuestManager(MMORpg plugin) {
        this.plugin = plugin;
        registerDefaultQuests();
    }

    private void registerDefaultQuests() {
        registerQuest(new Quest(
            "rat_exterminator",
            "Rat Exterminator",
            "Clear the cellar of pesky rats.",
            1,
            List.of(new QuestObjective(QuestObjectiveType.KILL, "RAT", 5, "Kill 5 rats")),
            Map.of("xp", 100, "gold", 50),
            true
        ));
    }

    public void registerQuest(Quest quest) {
        quests.put(quest.getId(), quest);
    }

    public Optional<Quest> getQuest(String id) {
        return Optional.ofNullable(quests.get(id));
    }

    public Collection<String> getQuestIds() {
        return quests.keySet();
    }

    public void acceptQuest(Player player, String questId) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null) return;

        Optional<Quest> questOpt = getQuest(questId);
        if (questOpt.isEmpty()) return;

        Quest quest = questOpt.get();
        if (profile.getLevel() < quest.getMinLevel()) {
            player.sendMessage(ColorUtils.colorize("&cYou need level " + quest.getMinLevel() + " for this quest!"));
            return;
        }

        if (profile.getActiveQuests().contains(questId) || profile.getCompletedQuests().contains(questId)) {
            player.sendMessage(ColorUtils.colorize("&cQuest already active or completed!"));
            return;
        }

        if (profile.getActiveQuests().size() >= plugin.getConfig().getInt("quests.max-active", 5)) {
            player.sendMessage(ColorUtils.colorize("&cMax active quests reached!"));
            return;
        }

        profile.addQuest(questId);
        player.sendMessage(ColorUtils.colorize("&aAccepted quest: &e" + quest.getName()));
    }

    public void updateProgress(Player player, QuestObjectiveType type, String target, int amount) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null) return;

        for (String questId : profile.getActiveQuests()) {
            Optional<Quest> questOpt = getQuest(questId);
            if (questOpt.isEmpty()) continue;
            Quest quest = questOpt.get();

            for (QuestObjective obj : quest.getObjectives()) {
                if (obj.getType() == type && obj.getTarget().equalsIgnoreCase(target)) {
                    profile.incrementQuestProgress(questId + "_" + obj.getTarget(), amount);
                    checkCompletion(player, quest);
                }
            }
        }
    }

    private void checkCompletion(Player player, Quest quest) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null) return;

        boolean allComplete = true;
        for (QuestObjective obj : quest.getObjectives()) {
            int progress = profile.getQuestProgress(quest.getId() + "_" + obj.getTarget());
            if (progress < obj.getRequiredAmount()) {
                allComplete = false;
                break;
            }
        }

        if (allComplete) {
            completeQuest(player, quest.getId());
        }
    }

    public void completeQuest(Player player, String questId) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile == null) return;

        Optional<Quest> questOpt = getQuest(questId);
        if (questOpt.isEmpty()) return;

        Quest quest = questOpt.get();
        profile.completeQuest(questId);

        for (Map.Entry<String, Integer> entry : quest.getRewards().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("xp")) {
                plugin.getExperienceManager().addExperience(player, entry.getValue(), "quest");
            }
        }

        player.sendMessage(ColorUtils.colorize("&6Quest completed: &e" + quest.getName() + " &6! Rewards: " + quest.getRewards()));
    }
}