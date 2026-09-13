package com.mmocrpg.quest;

import java.util.List;
import java.util.Map;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private final int minLevel;
    private final List<QuestObjective> objectives;
    private final Map<String, Integer> rewards;
    private final boolean repeatable;

    public Quest(String id, String name, String description, int minLevel, List<QuestObjective> objectives, Map<String, Integer> rewards, boolean repeatable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minLevel = minLevel;
        this.objectives = objectives;
        this.rewards = rewards;
        this.repeatable = repeatable;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getMinLevel() { return minLevel; }
    public List<QuestObjective> getObjectives() { return objectives; }
    public Map<String, Integer> getRewards() { return rewards; }
    public boolean isRepeatable() { return repeatable; }
}