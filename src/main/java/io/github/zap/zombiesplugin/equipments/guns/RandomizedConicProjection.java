package io.github.zap.zombiesplugin.equipments.guns;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import org.bukkit.World;

public class RandomizedConicProjection extends Gun{
    public RandomizedConicProjection(EquipmentData equipmentData, PlayerManager playerManager) {
        super(equipmentData, playerManager);
    }

    @Override
    public boolean shoot() {
        return false;
    }
}
