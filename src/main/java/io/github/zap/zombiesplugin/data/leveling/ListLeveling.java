package io.github.zap.zombiesplugin.data.leveling;

import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.ILeveling;

import java.util.Hashtable;
import java.util.List;

/**
 * Represent a collection of levels accessed by index (level)
 */
public class ListLeveling implements ILeveling {
    public List<Hashtable<String, IEquipmentValue>> levels;


    @Override
    public Hashtable<String, IEquipmentValue> getLevel(int level) {
        return levels.get(level);
    }

    @Override
    public int size() {
        return levels.size();
    }
}
