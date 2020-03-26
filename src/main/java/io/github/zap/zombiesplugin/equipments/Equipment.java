package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class Equipment extends HotbarObject {
    protected EquipmentData equipmentData;

    public Equipment(EquipmentData equipmentData) {
        this.equipmentData = equipmentData;
    }

    public EquipmentData getEquipmentData() {
        return equipmentData;
    }
}
