package io.github.zap.zombiesplugin.provider;

import com.google.gson.*;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.GunData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.ultvalue.EquipmentValue;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.DefaultWeaponVisual;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.LinearGun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class GunImporter extends Importer {
    private Hashtable<String, GunData> gunVault = new Hashtable<>();
    private Gson fileParser;
    private Hashtable<String, Class<? extends Gun>> behaviours = new Hashtable<>();

    @Override
    public void registerValue(String name, Object value) {
        Class<? extends Gun> typeCheck = (Class<? extends Gun>)value;
        if (typeCheck != null) {
            if (behaviours.containsKey(name)) {
                behaviours.replace(name, typeCheck);
            } else {
                behaviours.put(name, typeCheck);
            }
        }
    }

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        fileParser = manager.getGsonBuilder().create();
        registerBehaviours();
        generateDevelopmentGun();
    }

    // add gun behaviour here
    private void registerBehaviours() {
        registerValue("LinearGun", LinearGun.class);
    }

    @Override
    public void processConfigFile(Path file, String contents) {
        GunData data = fileParser.fromJson(contents, GunData.class);
        if(!gunVault.containsKey(data.id)) {
            gunVault.put(data.id, data);
        } else {
            String errorMessage = "Error: duplicate gun id or the gun already imported. Gun name: " + data.name + " at " + file.toString();
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, errorMessage);
        }
    }

    @Override
    public String getConfigExtension() {
        return "gunData";
    }

    public Gun createGun(String id) throws Exception {
        if (gunVault.containsKey(id)) {
            GunData currentData = gunVault.get(id);
            if(behaviours.containsKey(currentData.behaviour)) {
                Class<? extends Gun> bClazz = behaviours.get(currentData.behaviour);
                Gun gun = bClazz.getConstructor(GunData.class).newInstance(currentData);
                return gun;
            } else {
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find gun behaviour for this GunData: " + currentData);
            }
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find the gun id: " + id);
        }

        return null;
    }

    public Set<Map.Entry<String, GunData>> getGunDatas() {
        return gunVault.entrySet();
    }

    private void generateDevelopmentGun() {
        GunData data = new GunData();
        data.name = "Development Gun";
        data.id = "gun_dev";
        data.behaviour = "LinearGun";
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
            val.put("damage", new LoreEquipmentValue(10 * i / 2, "HP"));
            val.put("ammo", new LoreEquipmentValue(30 * i, ""));
            val.put("clipAmmo", new LoreEquipmentValue(10 * i, ""));
            val.put("fireRate", new LoreEquipmentValue(1 / i, "s", 1));
            val.put("reloadRate", new LoreEquipmentValue(5 / i, "s", 1));
            val.put("rewardGold", new LoreEquipmentValue(10, ""));
            val.put("range", new EquipmentValue(100));
            val.put("maxHitEntities", new LoreEquipmentValue(1 + i, ""));

            levels.levels.add(val);
        }
        data.levels = levels;

        finalize(data);
        gunVault.put(data.id, data);
    }

    private void finalize (GunData data) {
        data.defaultVisual.setDisplayColor(ChatColor.GOLD);
        data.defaultVisual.setInstruction(new String[] {
                ChatColor.YELLOW.toString() + "LEFT-CLICK " + ChatColor.GRAY + "to reload.",
                ChatColor.YELLOW.toString() + "RIGHT-CLICK" + ChatColor.GRAY + "to shoot."
        });
    }
}
