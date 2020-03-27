package io.github.zap.zombiesplugin.perks;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.UpgradeableEquipmentGroup;
import io.github.zap.zombiesplugin.placeholder.EquipmentPlaceHolder;

public class PerkObjectGroup extends UpgradeableEquipmentGroup {
    public PerkObjectGroup(boolean ignoreLevel) {
        super(ignoreLevel);
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        target.object = EquipmentPlaceHolder.createPerkPlaceHolder(target.itemGroupId + 1);
        target.object.init(target.slotId, player, player.getInventory().getHeldItemSlot() == target.slotId, true);
    }
}
