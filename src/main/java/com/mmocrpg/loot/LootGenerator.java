package com.mmocrpg.loot;

import com.mmocrpg.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.Random;

public class LootGenerator {
    private final Random random = new Random();

    public void generateLoot(LootTable table, Location location, int killerLevel) {
        World world = location.getWorld();
        if (world == null) return;

        if (random.nextDouble() > table.getGlobalChance()) return;

        for (LootTable.LootEntry entry : table.getEntries()) {
            if (killerLevel < entry.getMinLevel() || killerLevel > entry.getMaxLevel()) continue;
            if (random.nextDouble() > entry.getChance()) continue;

            int amount = entry.getMinAmount() == entry.getMaxAmount() ? entry.getMinAmount()
                    : random.nextInt(entry.getMaxAmount() - entry.getMinAmount() + 1) + entry.getMinAmount();

            ItemStack item = entry.getItem().create();
            item.setAmount(amount);
            world.dropItemNaturally(location, item);
        }
    }
}