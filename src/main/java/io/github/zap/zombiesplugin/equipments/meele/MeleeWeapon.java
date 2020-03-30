package io.github.zap.zombiesplugin.equipments.meele;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.MeleeSkill;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MeleeWeapon extends UpgradeableEquipment {
    protected MeleeSkill skill;

    public MeleeWeapon(EquipmentData equipmentData, PlayerManager playerManager) {
        super(equipmentData, playerManager);
    }

    @Override
    public boolean onRightClick(Block clickedBlock, BlockFace clickedFace) {
        if (skill != null && skill.canPerform()) {
            skill.perform();
        }

        return super.onRightClick(clickedBlock, clickedFace);
    }

    public MeleeSkill getSkill() {
        return skill;
    }

    public void setSkill(MeleeSkill skill) {
        this.skill = skill;
    }
}
