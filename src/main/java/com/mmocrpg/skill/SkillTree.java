package com.mmocrpg.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillTree {
    private final Map<String, Set<String>> tree = new HashMap<>();

    public void addDependency(String skillId, String prerequisiteId) {
        tree.computeIfAbsent(skillId.toLowerCase(), k -> new HashSet<>()).add(prerequisiteId.toLowerCase());
    }

    public boolean hasMetPrerequisites(String skillId, Set<String> unlockedSkills) {
        Set<String> prereqs = tree.get(skillId.toLowerCase());
        if (prereqs == null || prereqs.isEmpty()) {
            return true;
        }
        return unlockedSkills.containsAll(prereqs);
    }
}
