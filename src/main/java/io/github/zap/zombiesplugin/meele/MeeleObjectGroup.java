package io.github.zap.zombiesplugin.meele;

import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.UpgradeableEquipmentGroup;
import io.github.zap.zombiesplugin.placeholder.EquipmentPlaceHolder;
import org.bukkit.Material;

public class MeeleObjectGroup extends UpgradeableEquipmentGroup {
    public MeeleObjectGroup(boolean ignoreLevel) {
        super(ignoreLevel);
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        target.object = EquipmentPlaceHolder.createMeelePlaceHolder(target.itemGroupId + 1);
        target.object.init(target.slotId, player, player.getInventory().getHeldItemSlot() == target.slotId, true);
    }
}
