package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.data.IProvideDescription;
import io.github.zap.zombiesplugin.gamecreator.gui.CommonVisual;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum GameDifficulty implements IProvideDescription {
    NORMAL (Material.GREEN_TERRACOTTA,
            ChatColor.GREEN + "" + ChatColor.BOLD + "Difficulty" + ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + "Normal",
            1,
            Arrays.asList(ChatColor.GRAY + "Base difficulty recommended for" ,
                          ChatColor.GRAY + "newer and more casual players.")),
    HARD (Material.ORANGE_TERRACOTTA,
            ChatColor.GREEN + "" + ChatColor.BOLD + "Difficulty" + ChatColor.DARK_GRAY + " - " + ChatColor.RED + "Hard",
            1,
            Arrays.asList(ChatColor.GRAY + "Harder difficulty recommended" ,
                          ChatColor.GRAY + "for players looking for a",
                          ChatColor.GRAY + "challenge.")),
    RIP (Material.GREEN_TERRACOTTA,
            ChatColor.GREEN + "" + ChatColor.BOLD + "Difficulty" + ChatColor.DARK_GRAY + " - " + ChatColor.DARK_RED + "RIP",
            1,
            Arrays.asList(ChatColor.GRAY + "Not recommended. Proceed with" ,
                          ChatColor.GRAY + "caution!"));


    private final Material displayIcon;
    // Maybe in the future this map also have challenge mode
    // so we store them as formatted text
    private final String displayName;
    private final int displayAmount;
    private final List<String> description;


    GameDifficulty(Material displayIcon, String displayName, int displayAmount, List<String> description) {

        this.displayIcon = displayIcon;
        this.displayName = displayName;
        this.displayAmount = displayAmount;
        this.description = description;
    }

    @Override
    public ItemStack getDescriptionVisual() {
        return CommonVisual.createVisualItem(displayIcon, displayAmount, displayName, description);
    }
}
