package io.github.zap.zombiesplugin.equipments.perks;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import org.bukkit.entity.Player;

public abstract class Perk extends UpgradeableEquipment {
    public Perk(EquipmentData equipmentData) {
        super(equipmentData);
    }

    @Override
    public void init(int slot, Player player, boolean isSelected, boolean isVisible) {
        super.init(slot, player, isSelected, isVisible);
        onEquip();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        onUnEquip();
    }

    public abstract void onEquip();

    public abstract void onUnEquip();
}
