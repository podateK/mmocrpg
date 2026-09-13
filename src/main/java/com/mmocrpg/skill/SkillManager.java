package com.mmocrpg.skill;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SkillManager {
    private final Map<String, Skill> skills = new HashMap<>();

    public void registerSkill(Skill skill) {
        skills.put(skill.getId().toLowerCase(), skill);
    }

    public Optional<Skill> getSkill(String id) {
        return Optional.ofNullable(skills.get(id.toLowerCase()));
    }

    public Collection<Skill> getAllSkills() {
        return skills.values();
    }
}
