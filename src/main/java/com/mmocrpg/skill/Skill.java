package com.mmocrpg.skill;

import org.bukkit.entity.Player;
import java.util.List;

public interface Skill {
    String getId();
    String getName();
    String getDescription();
    int getManaCost();
    double getCooldown();
    int getRequiredLevel();
    List<String> getPrerequisites();
    void execute(Player player);
}
