package com.mmocrpg.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomItem {
    private final String id;
    private final String name;
    private final Material material;
    private final ItemRarity rarity;
    private final int levelRequirement;
    private final List<String> lore;
    private final Map<String, Integer> stats;

    public CustomItem(String id, String name, Material material, ItemRarity rarity, int levelRequirement, List<String> lore, Map<String, Integer> stats) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.levelRequirement = levelRequirement;
        this.lore = lore;
        this.stats = stats;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(rarity.getColor() + name);
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey("mmocrpg", "custom_item_id"), PersistentDataType.STRING, id);
            item.setItemMeta(meta);
        }
        return item;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public ItemRarity getRarity() { return rarity; }
    public int getLevelRequirement() { return levelRequirement; }
    public List<String> getLore() { return lore; }
    public Map<String, Integer> getStats() { return stats; }
}