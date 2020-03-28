package io.github.zap.zombiesplugin.meele;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.MeeleSkill;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MeeleWeapon extends UpgradeableEquipment {
    protected MeeleSkill skill;

    public MeeleWeapon(EquipmentData equipmentData) {
        super(equipmentData);
    }

    @Override
    public boolean onRightClick(Block clickedBlock, BlockFace clickedFace) {
        if (skill != null && skill.canPerform()) {
            skill.perform();
        }

        return super.onRightClick(clickedBlock, clickedFace);
    }
}
