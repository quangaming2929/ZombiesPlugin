package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;
import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;

/**
 * Base class for all data can be serialized
 */
public class ItemBasedObjectData implements ICustomSerializerIdentity {
    public String id;
    public String name;
    public Hashtable<String, String> customData;
}
