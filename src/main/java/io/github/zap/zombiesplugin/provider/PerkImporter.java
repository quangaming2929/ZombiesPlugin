package io.github.zap.zombiesplugin.provider;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.GunData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.ExpressionVisual;
import io.github.zap.zombiesplugin.equipments.Equipment;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.perks.Perk;
import io.github.zap.zombiesplugin.perks.SpeedPerk;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

public class PerkImporter extends Importer {
    private Hashtable<String, EquipmentData> perkVault = new Hashtable<>();
    private Hashtable<String, Class<? extends Perk>> behaviours = new Hashtable<>();

    public PerkImporter() {

        registerBehaviours();
        createTestSpeed();
    }

    private void registerBehaviours() {
        registerValue("Speed", SpeedPerk.class);
    }

    private void createTestSpeed() {
        EquipmentData perkData = new EquipmentData();
        perkData.id = "perk_speed_test";
        perkData.name = "Test speed";
        perkData.behaviour = "Speed";
        ExpressionVisual visual = new ExpressionVisual();
        visual.displayItem = Material.REDSTONE;
        visual.description = new String[] {"Gives speed ${speed}"};
        perkData.defaultVisual = visual;

        ListLeveling levels = new ListLeveling();
        levels.levels = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Hashtable<String, IEquipmentValue> val = new Hashtable<>();
            val.put(SpeedPerk.PERK_SPEED, new LoreEquipmentValue(i, ""));

            levels.levels.add(val);
        }

        perkData.levels = levels;

        finalize(perkData);
        perkVault.put(perkData.id, perkData);
    }

    public Perk getPerk(String id) throws Exception {
        if (perkVault.containsKey(id)) {
            EquipmentData currentData = perkVault.get(id);
            if(behaviours.containsKey(currentData.behaviour)) {
                Class<? extends Perk> bClazz = behaviours.get(currentData.behaviour);
                Perk perk = bClazz.getConstructor(EquipmentData.class).newInstance(currentData);
                return perk;
            } else {
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find perk behaviour for this EquipmentData: " + currentData);
            }
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find the perk id: " + id);
        }

        return null;
    }

    @Override
    public void registerValue(String name, Object value) {
        Class<? extends Perk> typeCheck = (Class<? extends Perk>)value;
        if (typeCheck != null) {
            if (behaviours.containsKey(name)) {
                behaviours.replace(name, typeCheck);
            } else {
                behaviours.put(name, typeCheck);
            }
        }
    }

    @Override
    public void processConfigFile(Path file, String contents) {

    }

    @Override
    public String getConfigExtension() {
        return "perkData";
    }


    private void finalize(EquipmentData data) {
        data.defaultVisual.setDisplayColor(ChatColor.DARK_BLUE);
        data.defaultVisual.setInstruction( new String[] {""  + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Active until death."});
    }
}
