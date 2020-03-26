package io.github.zap.zombiesplugin.data;

import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;

public class EquipmentData {
    public IDefaultVisual defaultVisual;
    public String id;
    public ILeveling levels;
    public Hashtable<String, String> customData;
    public String behaviour;

    /**
     * Get default visual state for non-ultimate-able equipment
     */
    public ItemStack getDefaultVisual() {
        return getDefaultVisual(0);
    }

    /**
     * Get default visual state for ultimate-able equipment
     */
    public ItemStack getDefaultVisual(int level) {
        return defaultVisual.getDefaultVisual(level, levels);
    }


    /**
     * Get stats for non-ultimate-able equipment
     */
    public Hashtable<String, IEquipmentValue> getStats() {
        return getStats(0);
    }

    /**
     * Get stats for ultimate-able equipment
     */
    public Hashtable<String, IEquipmentValue> getStats(int level) {
        if (level >= 0 && level < levels.size()) {
            return levels.getLevel(level);
        } else {
            throw new IndexOutOfBoundsException("Invalid level value. Expected range: 0 -> " + (levels.size() - 1));
        }
    }

    /**
     * Get this equipment defined data
     * @param name the custom data name
     */
    public String getCustomData (String name) {
        return customData.get(name);
    }

    /**
     * Get this equipment id
     * @return
     */
    public String getId() {
        return id;
    }
}
