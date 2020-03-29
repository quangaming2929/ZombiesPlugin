package io.github.zap.zombiesplugin.provider.equipments;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.MeeleData;
import io.github.zap.zombiesplugin.data.MeeleSkill;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.visuals.DefaultWeaponVisual;
import io.github.zap.zombiesplugin.equipments.Equipment;
import io.github.zap.zombiesplugin.equipments.meele.MeleeWeapon;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

public class MeleeImporter extends EquipmentImporter {

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerSkills();
        createTestMelee();
    }

    private void registerSkills() {
        // Example:
        // dataVault.put("meele_noSkill", NoSkill.class); // Since no skill are represent by null so no need :)
    }

    @Override
    public String getConfigExtension() {
        return "meeleData";
    }

    @Override
    protected Class<? extends EquipmentData> getConfigType() {
        return MeeleData.class;
    }

    @Override
    public Equipment createEquipment(String id, PlayerManager manager) throws Exception {
        if (dataVault.containsKey(id)) {
            EquipmentData currentData = dataVault.get(id);
            MeleeWeapon meele = new MeleeWeapon(currentData, manager);
            if(currentData.behaviour != null && values.containsKey(currentData.behaviour)) {
                Class skillClazz = values.get(currentData.behaviour);
                MeeleSkill skill = (MeeleSkill) skillClazz.getConstructor(MeleeWeapon.class).newInstance(meele);
                meele.setSkill(skill);
            }

            return meele;
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find the equipment id: " + id);
        }

        return null;
    }

    private void createTestMelee() {
        EquipmentData knifeTest = new EquipmentData();
        knifeTest.id = "melee_knife_test";
        knifeTest.name = "Knife (Testing Melee)";

        DefaultWeaponVisual visual = new DefaultWeaponVisual();
        visual.displayItem = Material.IRON_SWORD;
        visual.description = new String[] {
                "" + ChatColor.GRAY + "Basic melee weapon. It is not",
                "" + ChatColor.GRAY + "recommended to use this",
                "" + ChatColor.GRAY + "regularly"
        };

        knifeTest.defaultVisual = visual;

        ListLeveling levels = new ListLeveling();
        levels.levels = new ArrayList<>();
        levels.levels.add(new Hashtable<>());

        knifeTest.levels = levels;
        finalize(knifeTest);

        dataVault.put(knifeTest.id, knifeTest);
    }

    @Override
    protected void finalize(EquipmentData data) {
        data.defaultVisual.setDisplayColor(ChatColor.GREEN);

        // prevent config file want to instruct something different
        if(data.defaultVisual.getInstruction() != null && data.defaultVisual.getInstruction().length == 0){
            data.defaultVisual.setInstruction(new String[] {
                    ChatColor.YELLOW.toString() + "LEFT-CLICK " + ChatColor.GRAY + "to strike."
            });
        }
    }
}
