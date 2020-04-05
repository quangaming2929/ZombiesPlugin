package io.github.zap.zombiesplugin.provider.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.ultvalue.EquipmentValue;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.ExpressionVisual;
import io.github.zap.zombiesplugin.equipments.skills.HealSkill;
import io.github.zap.zombiesplugin.equipments.skills.Skill;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class SkillImporter extends EquipmentImporter {

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerBehaviours();
        generateTestHealSkill();
    }

    private void generateTestHealSkill() {
        EquipmentData healSkillTest = new EquipmentData();
        healSkillTest.id = "skill_heal_test";
        healSkillTest.name = "Heal Skill (Skill Test)";
        healSkillTest.behaviour = "HealSkill";

        ExpressionVisual visual = new ExpressionVisual();
        visual.displayItem = Material.GOLDEN_APPLE;
        visual.description = new String[] {
                "Heals you for up to " + ChatColor.RED + "${" + HealSkill.SKILL_HEAL_SELF + "} " + ChatColor.GRAY + "and",
                "nearby teammates for up to",
                ChatColor.RED + "${" + HealSkill.SKILL_HEAL_TEAM + "}" + ChatColor.GRAY + "."
        };
        healSkillTest.defaultVisual = visual;

        ListLeveling levels = new ListLeveling();
        levels.levels = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Hashtable<String, IEquipmentValue> val = new Hashtable<>();
            val.put(HealSkill.SKILL_HEAL_SELF, new LoreEquipmentValue(8 * (i / 2f), "Heal Self", "❤", 0));
            val.put(HealSkill.SKILL_HEAL_TEAM, new LoreEquipmentValue(4 * (i / 2f), "Heal Self", "❤", 0));
            val.put(HealSkill.SKILL_HEAL_RADIUS, new EquipmentValue(5 * (i / 2f)));
            val.put(Skill.SKILL_CD, new EquipmentValue(30 / i));

            levels.levels.add(val);
        }
        healSkillTest.levels = levels;

        finalize(healSkillTest);
        dataVault.put(healSkillTest.id, healSkillTest);
    }

    private void registerBehaviours() {
        registerValue("HealSkill", HealSkill.class);
    }

    @Override
    protected void finalize(EquipmentData data) {
        data.defaultVisual.setDisplayColor(ChatColor.AQUA);
    }

    @Override
    public String getConfigExtension() {
        return "skillData";
    }
}
