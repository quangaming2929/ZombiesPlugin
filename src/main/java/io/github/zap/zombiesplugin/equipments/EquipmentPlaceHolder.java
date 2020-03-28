package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EquipmentPlaceHolder extends HotbarObject {

    // Simple-Factory
    public static EquipmentPlaceHolder createGunPlaceHolder(int slotNo) {
        String displayName = ChatColor.GOLD + "Gun #" + slotNo;
        Material displayItem = Material.LIGHT_GRAY_DYE;
        String[] lore = new String[] {
                ChatColor.GRAY.toString() + "Purchase guns at " + ChatColor.GOLD + "Shops " + ChatColor.GRAY + "or at",
                ChatColor.GRAY.toString() + "the " + ChatColor.DARK_PURPLE + "Lucky Chest" + ChatColor.GRAY + "!"
        } ;

        EquipmentPlaceHolder e = new EquipmentPlaceHolder(displayItem, displayName, lore);
        return e;
    }

    public static EquipmentPlaceHolder createPerkPlaceHolder(int slotNo) {
        String displayName = ChatColor.YELLOW + "Perk #" + slotNo;
        Material displayItem = Material.GRAY_DYE;
        String[] lore = new String[] {
                "" + ChatColor.GRAY + "Buy perks at " + ChatColor.DARK_BLUE + "Perk Machines" + ChatColor.GRAY + ".",
                "" + ChatColor.GRAY + "Perk Machines require the ",
                "" + ChatColor.GOLD + "Power " + ChatColor.GRAY + "to be on. Find and",
                "" + ChatColor.GRAY + "activate the " + ChatColor.GOLD + "Power Switch " + ChatColor.GRAY + "in",
                "" + ChatColor.GRAY + "the " + ChatColor.GREEN + "Power Station " + ChatColor.GRAY + "in order",
                "" + ChatColor.GRAY + "to buy perks!"

        };

        EquipmentPlaceHolder e = new EquipmentPlaceHolder(displayItem, displayName, lore);
        return e;
    }

    public static EquipmentPlaceHolder createPerkVendingPlaceHolder(int slotNo) {
        String displayName = ChatColor.YELLOW + "Perk #" + slotNo;
        Material displayItem = Material.GRAY_DYE;
        String[] lore = new String[] {
                "" + ChatColor.GRAY + "Buy perks at the " + ChatColor.DARK_BLUE + "Vending",
                "" + ChatColor.DARK_BLUE + "Machine"
        };

        EquipmentPlaceHolder e = new EquipmentPlaceHolder(displayItem, displayName, lore);
        return e;
    }


    public static EquipmentPlaceHolder createMeleePlaceHolder(int slotNo) {
        String displayName = ChatColor.GREEN + "Melee Weapon #" + slotNo;
        Material displayItem = Material.BLACK_DYE;
        String[] lore = new String[] {
                "" + ChatColor.GRAY + "Secondary weapon to help you",
                "" + ChatColor.GRAY + " defend yourself. Buy Melee"  ,
                "" + ChatColor.GRAY + "weapon at the " + ChatColor.GOLD + "Lucky Chest" + ChatColor.GRAY + "!"
        };

        EquipmentPlaceHolder e = new EquipmentPlaceHolder(displayItem, displayName, lore);
        return e;
    }

    public static EquipmentPlaceHolder createSkillPlaceHolder(int slotNo) {
        String displayName = ChatColor.AQUA + "Skill #" + slotNo;
        Material displayItem = Material.GRAY_DYE;
        String[] lore = new String[] {
                "" + ChatColor.AQUA + "Skills " + ChatColor.GRAY + "are special reusable",
                "" + ChatColor.GRAY + "abilities. Find Skills in the",
                "" + ChatColor.DARK_PURPLE + "Lucky Chest" + ChatColor.GRAY + "!"
        };

        EquipmentPlaceHolder e = new EquipmentPlaceHolder(displayItem, displayName, lore);
        return e;
    }

    private String displayName;
    private String[] lore;
    private Material displayItem;

    public EquipmentPlaceHolder(Material displayItem, String displayName, String... lore) {
        this.displayItem = displayItem;
        this.displayName = displayName;
        this.lore = lore;
    }

    @Override
    public void init(int slot, Player player,  boolean isSelected, boolean isVisible) {
        super.init(slot, player, isSelected, isVisible);
        ItemStack item = new ItemStack(displayItem, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(getLore()));
        item.setItemMeta(meta);
        setSlot(item);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getLore() {
        return lore;
    }

    public Material getDisplayItem() {
        return displayItem;
    }
}
