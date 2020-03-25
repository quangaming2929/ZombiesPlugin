package io.github.zap.zombiesplugin.data;

import java.util.Hashtable;

public interface ILeveling {
    Hashtable<String, IEquipmentValue> getLevel(int level);
    int size();
}
