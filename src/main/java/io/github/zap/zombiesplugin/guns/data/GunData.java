package io.github.zap.zombiesplugin.guns.data;

import io.github.zap.zombiesplugin.guns.data.gunattributes.BaseFeature;
import io.github.zap.zombiesplugin.utils.RomanNumber;
import io.github.zap.zombiesplugin.utils.WeaponStatsLoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Hashtable;
import java.util.List;

public class  GunData {
    public String name = "Default gun";
    public Material displayItem = Material.BARRIER;
    public Vector knockBack; // knockback is not a good word here and is it a vector because how zapper kb
    public int rewardGold;
    public String[] description = new String[] {
            "This is a default gun description",
            "This will be parsed from a config file" };

    private Hashtable<String, String> gunFeature;
    public BaseFeature feature;

    // represent gun leveling
    // index:
    //  0 : base stats
    //  1+ : ultimate levels
    public List<BulletStats> stats;

    public void updateFeatureInfo(String field, String value) {
        gunFeature.replace(field, value);
    }

    public ItemStack getDefaultVisual(int level, ItemStack overrideStack) {

        ItemStack item = (overrideStack == null) ? new ItemStack(displayItem, 1) : overrideStack;
        ItemMeta meta = item.getItemMeta();

        setDisplayName(level, meta);
        meta.setLore(getLore(0).build());

        return item;
    }

    protected void setDisplayName(int level, ItemMeta meta) {


        if (level > 0) {
            String displayName = ChatColor.GOLD.toString() + ChatColor.BOLD + name + " Ultimate";
            if (stats.size() > 2) {
                displayName += RomanNumber.toRoman(level);
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
