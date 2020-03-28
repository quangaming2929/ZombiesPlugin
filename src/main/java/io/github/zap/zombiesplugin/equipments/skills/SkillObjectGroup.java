package io.github.zap.zombiesplugin.equipments.skills;

import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.UpgradeableEquipmentGroup;
import io.github.zap.zombiesplugin.equipments.EquipmentPlaceHolder;

public class SkillObjectGroup extends UpgradeableEquipmentGroup {
    public SkillObjectGroup(boolean ignoreLevel) {
        super(ignoreLevel);
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        target.object = EquipmentPlaceHolder.createSkillPlaceHolder(target.itemGroupId + 1);
        target.object.init(target.slotId, player, player.getInventory().getHeldItemSlot() == target.slotId, true);
    }
}
