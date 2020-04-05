package io.github.zap.zombiesplugin.provider.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.GunData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.ultvalue.EquipmentValue;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.DefaultWeaponVisual;
import io.github.zap.zombiesplugin.equipments.guns.LinearGun;
import io.github.zap.zombiesplugin.equipments.guns.RandomizedConicProjection;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.utils.IOHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class GunImporter extends EquipmentImporter {

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerBehaviours();
        generateDevelopmentGun();
    }

    @Override
    protected Class<? extends EquipmentData> getConfigType() {
        return GunData.class;
    }

    // add gun behaviour here
    private void registerBehaviours() {
        registerValue("LinearGun", LinearGun.class);
        registerValue("RandomizedConicProjection", RandomizedConicProjection.class);
    }


    @Override
    public String getConfigExtension() {
        return "gunData";
    }


    private void generateDevelopmentGun() {
        GunData data = new GunData();
        data.name = "Development Gun";
        data.id = "gun_dev";
        data.behaviour = "RandomizedConicProjection";
        // Custom data
        data.customData = new Hashtable<>();
        data.customData.put("particle", Particle.CRIT.toString());

        DefaultWeaponVisual dv = new DefaultWeaponVisual();
        dv.displayItem = Material.IRON_HOE;
        dv.description = new String[] {
                "This gun is used for development. Please",
                "remove this before production lifecycle"
        };
        data.defaultVisual = dv;

        ListLeveling levels = new ListLeveling();
        levels.levels = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Hashtable<String, IEquipmentValue> val = new Hashtable<>();
            val.put("damage", new LoreEquipmentValue(10 * i / 2, "Damage", "HP"));
            val.put("ammo", new LoreEquipmentValue(30 * i, "Ammo",""));
            val.put("clipAmmo", new LoreEquipmentValue(10 * i, "Clip Ammo", ""));
            val.put("fireRate", new LoreEquipmentValue(1 / i, "Fire rate", "s", 1));
            val.put("reloadRate", new LoreEquipmentValue(5 / i, "Reload Rate", "s", 1));
            val.put("rewardGold", new EquipmentValue(10));
            val.put("gunRange", new EquipmentValue(100));
            val.put("maxHitEntities", new EquipmentValue(1 + i));

            levels.levels.add(val);
        }
        data.levels = levels;

        finalize(data);
        dataVault.put(data.id, data);
    }

    @Override
    protected void finalize (EquipmentData data) {
        data.defaultVisual.setDisplayColor(ChatColor.GOLD);

        // prevent config file want to instruct something different
        if(data.defaultVisual.getInstruction() != null && data.defaultVisual.getInstruction().length == 0) {
            data.defaultVisual.setInstruction(new String[] {
                    ChatColor.YELLOW.toString() + "LEFT-CLICK " + ChatColor.GRAY + "to reload.",
                    ChatColor.YELLOW.toString() + "RIGHT-CLICK" + ChatColor.GRAY + "to shoot."
            });
        }
    }
}
