package com.mmocrpg.loot;

import com.mmocrpg.item.CustomItem;
import com.mmocrpg.item.ItemRarity;
import java.util.List;
import java.util.Map;

public class LootTable {
    private final String id;
    private final List<LootEntry> entries;
    private final double globalChance;

    public LootTable(String id, List<LootEntry> entries, double globalChance) {
        this.id = id;
        this.entries = entries;
        this.globalChance = globalChance;
    }

    public String getId() { return id; }
    public List<LootEntry> getEntries() { return entries; }
    public double getGlobalChance() { return globalChance; }

    public static class LootEntry {
        private final CustomItem item;
        private final int minAmount;
        private final int maxAmount;
        private final double chance;
        private final int minLevel;
        private final int maxLevel;

        public LootEntry(CustomItem item, int minAmount, int maxAmount, double chance, int minLevel, int maxLevel) {
            this.item = item;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.chance = chance;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }

        public CustomItem getItem() { return item; }
        public int getMinAmount() { return minAmount; }
        public int getMaxAmount() { return maxAmount; }
        public double getChance() { return chance; }
        public int getMinLevel() { return minLevel; }
        public int getMaxLevel() { return maxLevel; }
    }
}