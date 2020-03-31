package io.github.zap.zombiesplugin.equipments.meele;

import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.UpgradeableEquipmentGroup;
import io.github.zap.zombiesplugin.equipments.EquipmentPlaceHolder;

public class MeeleObjectGroup extends UpgradeableEquipmentGroup {
    public MeeleObjectGroup(boolean ignoreLevel) {
        super(ignoreLevel);
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        target.object = EquipmentPlaceHolder.createMeleePlaceHolder(target.itemGroupId + 1);
        target.object.init(target.slotId, player, player.getInventory().getHeldItemSlot() == target.slotId, true);
    }
}
