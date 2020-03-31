package io.github.zap.zombiesplugin.shop.machine.data;

import org.bukkit.Material;

/**
 * Abbreviation for: Team Machine Task Data
 * This is a class that represent Team Machine Task from config file
 */
// TODO: Refactor with equipment lore builder <Default Visual>
public class TMTaskData {
    public String displayName;
    public Material displayItem;
    public String[] lore;
    public String task;
    public ICost cost;


    // TODO: Add sound fx
}
