package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;

import java.util.Hashtable;

public interface ILeveling extends ICustomSerializerIdentity {
    Hashtable<String, IEquipmentValue> getLevel(int level);
    int size();
}
