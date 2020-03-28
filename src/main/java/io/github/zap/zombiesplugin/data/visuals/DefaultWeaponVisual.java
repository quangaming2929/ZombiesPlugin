package io.github.zap.zombiesplugin.data.visuals;

import io.github.zap.zombiesplugin.data.IDefaultVisual;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.ILeveling;
import io.github.zap.zombiesplugin.data.IIncludeLore;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/* TODO: To prevent class explosion, we will define color and instruction in Importers class
 *  - Gun:   ChatColor.GOLD;
 *           ChatColor.YELLOW.toString() + "LEFT-CLICK " + ChatColor.GRAY + "to reload.",
 *           ChatColor.YELLOW.toString() + "RIGHT-CLICK" + ChatColor.GRAY + "to shoot."
 *  - Meele: ChatColor.GREEN;
 *           ChatColor.YELLOW.toString() + "LEFT-CLICK " + ChatColor.GRAY + "to strike."
 */

/**
 * A class define equipments default visual state include display item and name,
 * description, a stats list and an instruction
 */
public class DefaultWeaponVisual implements IDefaultVisual {
    public Material displayItem;
    public ChatColor displayColor;
    public String[] instruction;

    public String[] description;


    @Override
    public ItemStack getDefaultVisual(String name, int level, ILeveling levels) {
        Hashtable<String, IEquipmentValue> currentLevel = levels.getLevel(level);
        ItemStack item = new ItemStack(getDisplayItem(), 1);
        ItemMeta meta = item.getItemMeta();

        WeaponStatsLoreBuilder builder = new WeaponStatsLoreBuilder();
        builder.withDescriptions(description);

        if (level > 0) {
            Hashtable<String, IEquipmentValue> previousLevel = levels.getLevel(level - 1);

            // Visual for ultimate
            String displayName = "" + ChatColor.BOLD + getDisplayColor() + name + "Ultimate ";
            if (levels.size() > 1) {
                // Has multiple ultimate level?
                displayName += RomanNumber.toRoman(level);
            }

            meta.setDisplayName(displayName);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);

            AddWeaponStats(builder, currentLevel, previousLevel);
        } else {
            // Visual for normal
            meta.setDisplayName("" + getDisplayColor() + name);

            // We use an empty hash table so that the method will never show previous value
            AddWeaponStats(builder, currentLevel, new Hashtable<>());
        }
        builder.withInstruction(getInstruction());
        meta.setLore(builder.build());

        item.setItemMeta(meta);
        return item;
    }

    private void AddWeaponStats(WeaponStatsLoreBuilder builder,
                                Hashtable<String, IEquipmentValue> currentLevel,
                                Hashtable<String, IEquipmentValue> previousLevel) {
        for (Map.Entry<String, IEquipmentValue> currentEntry : currentLevel.entrySet()) {
            if(currentEntry.getValue() instanceof IIncludeLore) {
                // Prepare the current value
                IIncludeLore currentIL = (IIncludeLore) currentEntry.getValue();
                String statName = currentEntry.getKey();
                String currentVal = currentIL.getDisplayValue();

                if (previousLevel.containsKey(statName)) {
                    IEquipmentValue previousEV = previousLevel.get(statName);
                    if(previousEV instanceof IIncludeLore) {
                        // Prepare the previous value
                        IIncludeLore previousIL = (IIncludeLore)previousEV;
                        String previousVal = previousIL.getDisplayValue();

                        // Add stats for both old and new val
                        builder.addStats(currentIL.getStatsName(), previousVal, currentVal);
                        continue;
                    }
                }

                // Add stats for only new val
                builder.addStats(currentIL.getStatsName(), currentVal);
            }
        }
    }

    @Override
    public Material getDisplayItem() {
        return displayItem;
    }

    @Override
    public ChatColor getDisplayColor() {
        return displayColor;
    }

    @Override
    public String[] getInstruction() {
        return instruction;
    }

    @Override
    public void setDisplayItem(Material item) {
        displayItem = item;
    }

    @Override
    public void setDisplayColor(ChatColor color) {
        displayColor = color;
    }

    @Override
    public void setInstruction(String[] instruction) {
        this.instruction = instruction;
    }
}
