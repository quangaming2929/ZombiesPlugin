package io.github.zap.zombiesplugin.data;

import org.bukkit.Material;

import java.util.Hashtable;
import java.util.List;

/**
 * Abbreviation for: Team Machine Task Data
 * This is a class that represent Team Machine Task from config file
 */
// TODO: Refactor with equipment lore builder <Default Visual>
public class TMTaskData extends ItemBasedObjectData{
    public ICost cost;
    public String taskName;
    public Material displayItem;
    public List<String> description;
    public SoundFx purchaseFx;
}
