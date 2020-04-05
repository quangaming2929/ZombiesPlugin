package io.github.zap.zombiesplugin.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeaponStatsLoreBuilder {
    private final List<String> descriptions;
    private final List<String> stats;
    private final List<String> instructions;

    public WeaponStatsLoreBuilder() {
        this.descriptions = new ArrayList<String>();
        this.stats = new ArrayList<String>();
        this.instructions = new ArrayList<String>();
    }

    public static String[] getGunPlaceHolderLore() {
        return new String[] {
                ChatColor.GRAY.toString() + "Purchase guns at " + ChatColor.GOLD + "Shops " + ChatColor.GRAY + "or at",
                ChatColor.GRAY.toString() + "the " + ChatColor.DARK_PURPLE + "Lucky Chest" + ChatColor.GRAY + "!"
        };
    }

    /**
     * Set the description lore of the weapon
     * @param descriptions the weapon instruction
     * @return current builder
     */
    public WeaponStatsLoreBuilder withDescriptions(String... descriptions) {
        this.descriptions.clear();
        Collections.addAll(this.descriptions, descriptions);

        return this;
    }

    /**
     * Set the description lore of the weapon
     * @param description a line of the weapon instruction
     * @return current builder
     */
    public WeaponStatsLoreBuilder addDescription(String description) {
        this.descriptions.add(description);

        return this;
    }

    /**
     * Add the lore stats of the weapon for ultimate value.
     * @param name stats name
     * @param oldValue the previous level stats
     * @param newValue the current level stats
     * @return current builder
     */
    public WeaponStatsLoreBuilder addStats(String name, Object oldValue, Object newValue) {
        stats.add(ChatColor.DARK_GRAY.toString() + " ◼ " + ChatColor.GRAY + name + ": " + ChatColor.DARK_GRAY + oldValue +  " " + "➔ " + ChatColor.GREEN + newValue + " " );
        return this;
    }

    /**
     * Add the lore of the weapon for non ultimate value.
     * @param name stats name
     * @param value the previous level stats
     * @return current builder
     */
    public WeaponStatsLoreBuilder addStats(String name, Object value) {
        stats.add(ChatColor.DARK_GRAY.toString() + " ◼ " + ChatColor.GRAY + name + ": " + ChatColor.GREEN + value + " ");

        return this;
    }

    /**
     * Set the instruction lore of the weapon
     * @param instructions the weapon instruction
     * @return current builder
     */
    public WeaponStatsLoreBuilder withInstruction (String... instructions) {
        this.instructions.clear();
        if(instructions != null) {
            Collections.addAll(this.instructions, instructions);
        }

        return this;
    }

    /**
     * Set the instruction lore of the weapon
     * @param instructions the weapon instruction
     * @return current builder
     */
    public WeaponStatsLoreBuilder withInstruction (List<String> instructions) {
        this.instructions.clear();
        this.instructions.addAll(instructions);
        return this;
    }

    /**
     * Set the instruction lore of the weapon
     * @param instruction a line of the weapon instruction
     * @return current builder
     */
    public WeaponStatsLoreBuilder addInstruction(String instruction ) {
        this.instructions.add(instruction);

        return this;
    }


    /**
     * Build the weapon lore
     * @return a string array represent weapon lore
     */
    public List<String> build() {
        List<String> lore = new ArrayList<String>();
        for (String item : descriptions) {
            lore.add(ChatColor.GRAY + item);
        }

        if (stats.size() > 0) {
            lore.add("");
        }

        for (String item : stats) {
            lore.add(item);
        }

        if (instructions.size() > 0) {
            lore.add("");
        }

        for (String item : instructions) {
            lore.add(item);
        }

        return lore;
    }
}
