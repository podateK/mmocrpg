package com.mmocrpg.quest;

import java.util.Map;

public class QuestObjective {
    private final QuestObjectiveType type;
    private final String target;
    private final int requiredAmount;
    private final String description;

    public QuestObjective(QuestObjectiveType type, String target, int requiredAmount, String description) {
        this.type = type;
        this.target = target;
        this.requiredAmount = requiredAmount;
        this.description = description;
    }

    public QuestObjectiveType getType() { return type; }
    public String getTarget() { return target; }
    public int getRequiredAmount() { return requiredAmount; }
    public String getDescription() { return description; }
}