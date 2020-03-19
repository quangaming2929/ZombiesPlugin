package io.github.zap.zombiesplugin.guns.data;

import io.github.zap.zombiesplugin.guns.data.gunattributes.GunAttribute;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class  GunData {
    public String name = "Default gun";
    public Material displayItem = Material.BARRIER;
    public SoundFx gunFx;
    public int rewardGold;
    public String[] description = new String[] {
            "This is a default gun description",
            "This will be parsed from a config file" };

    public GunAttribute feature;

    // represent gun leveling
    // index:
    //  0 : base stats
    //  1+ : ultimate levels
    public List<BulletStats> stats;

    public ItemStack getDefaultVisual(int level, ItemStack overrideStack) {

        ItemStack item = (overrideStack == null) ? new ItemStack(displayItem, 1) : overrideStack;
        item.setType(displayItem);
        ItemMeta meta = item.getItemMeta();

        setDisplayName(level, meta);
        meta.setLore(getLore(level).build());
        if(level > 0) {
            if(meta.getEnchants().size() == 0) {
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            }
        } else {
            for (Enchantment etch : meta.getEnchants().keySet() ) {
                meta.removeEnchant(etch);
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    protected void setDisplayName(int level, ItemMeta meta) {

        if (level > 0) {
            String displayName = ChatColor.GOLD.toString() + ChatColor.BOLD + name + " Ultimate";
            if (stats.size() > 2) {
                displayName += " " + RomanNumber.toRoman(level);
            }

            meta.setDisplayName(displayName);
        } else {
            meta.setDisplayName(ChatColor.GOLD + name);
        }
    }

    protected WeaponStatsLoreBuilder getLore(int level) {
        WeaponStatsLoreBuilder builder = new WeaponStatsLoreBuilder()
                .withDescriptions(description)
                .withInstruction(WeaponStatsLoreBuilder.getGunInstructionLore());


        BulletStats currentStats = stats.get(level);
        if(level > 0) {
            BulletStats previousStats = stats.get(level - 1);

            builder.addStats("Damage", previousStats.baseDamage, currentStats.baseDamage, "HP")
                    .addStats("Ammo", previousStats.baseAmmoSize, currentStats.baseAmmoSize, "")
                    .addStats("Clip Ammo", previousStats.baseClipAmmoSize, currentStats.baseClipAmmoSize, "" )
                    .addStats("Fire Rate", previousStats.fireRate, currentStats.fireRate, "s")
                    .addStats("Reload", previousStats.reloadRate, currentStats.reloadRate, "s");
        } else {
            builder.addStats("Damage", currentStats.baseDamage, "HP")
                    .addStats("Ammo", currentStats.baseAmmoSize, "")
                    .addStats("Clip Ammo", currentStats.baseClipAmmoSize, "" )
                    .addStats("Fire Rate", currentStats.fireRate, "s")
                    .addStats("Reload", currentStats.reloadRate, "s");
        }

        return builder;
    }
}
