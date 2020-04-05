package io.github.zap.zombiesplugin.provider.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.ExpressionVisual;
import io.github.zap.zombiesplugin.equipments.perks.SpeedPerk;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Hashtable;

public class PerkImporter extends EquipmentImporter {

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerBehaviours();
        createTestSpeed();
    }

    private void registerBehaviours() {
        registerValue("Speed", SpeedPerk.class);
    }


    @Override
    public String getConfigExtension() {
        return "perkData";
    }

    @Override
    protected void finalize(EquipmentData data) {
        data.defaultVisual.setDisplayColor(ChatColor.DARK_BLUE);
        if(data.defaultVisual.getInstruction() != null && data.defaultVisual.getInstruction().length == 0) {
            data.defaultVisual.setInstruction( new String[] {
                    ""  + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Active until death."
            });
        }
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
        dataVault.put(perkData.id, perkData);
    }
}
