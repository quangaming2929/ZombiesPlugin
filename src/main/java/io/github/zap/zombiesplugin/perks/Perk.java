package io.github.zap.zombiesplugin.perks;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;

public abstract class Perk extends UpgradeableEquipment {
    public Perk(EquipmentData equipmentData) {
        super(equipmentData);
    }

    public abstract void onEquip();

    public abstract void onUnEquip();
}
