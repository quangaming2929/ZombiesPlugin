package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;

public abstract class Gun extends UpgradeableEquipment {

    public Gun(EquipmentData equipmentData) {
        super(equipmentData);
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(level);

    }
}
