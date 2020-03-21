package io.github.zap.zombiesplugin.provider;

import com.google.gson.*;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.LinearGun;
import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.guns.data.CustomValue;
import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.leveling.UltimateLevelingList;
import io.github.zap.zombiesplugin.player.GunUser;
import org.bukkit.Material;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
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
        if(!gunVault.containsKey(data.name)) {
            gunVault.put(data.id, data);
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, String.format("Error duplicate gun id or the gun already imported. Gun name: %s (%s)" + data.name + file));
        }
    }

    @Override
    public String getConfigExtension() {
        return "gunData";
    }

    public Gun createGun(String id, GunUser user) throws Exception {
        if (gunVault.containsKey(id)) {
            GunData currentData = gunVault.get(id);
            if(behaviours.containsKey(currentData.gunBehaviour)) {
                Class<? extends Gun> bClazz = behaviours.get(currentData.gunBehaviour);
                Gun gun = bClazz.getConstructor(GunData.class, GunUser.class).newInstance(currentData, user);
                return gun;
            } else {
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find gun behaviour for this GunData: " + currentData);
            }
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find the gun id: " + id);
        }

        return null;
    }

    private void generateDevelopmentGun() {
        GunData data = new GunData();
        data.description = new String[] { "TestGun" , "Test object will be remove later" };
        data.displayItem = Material.IRON_HOE;
        data.name = "Development gun";
        data.id = "gun_dev";
        data.gunBehaviour = "LinearGun";

        // stats
        ArrayList<BulletStats> stats = new ArrayList<BulletStats>();
        for (int i = 0; i < 4; i++) {
            BulletStats s = new BulletStats();
            s.baseAmmoSize = (int)(20 + 20 * (float)i / 2);
            s.baseClipAmmoSize = (int)(10 + 20 * (float) i / 2);
            s.baseDamage = 10 + 20 * (float)i / 2;
            s.baseRange = 100 + 20 * (float)i / 2;
            s.baseFireRate = 0.25;
            s.baseReloadRate = 1;
            s.baseRewardGold = 10;
            s.setCustomValue("maxHitEntities", new CustomValue("2"));
            s.setCustomValue("particle", new CustomValue("CRIT"));
            stats.add(s);
        }

        UltimateLevelingList ultVal = new UltimateLevelingList();
        ultVal.levels = stats;
        data.stats = ultVal;

        gunVault.put("gun_dev", data);
    }
}
