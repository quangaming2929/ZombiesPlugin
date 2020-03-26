package io.github.zap.zombiesplugin.equipments;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.IEquipmentValue;
import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;

public abstract class UpgradeableEquipment extends Equipment {
    private int level;

    public UpgradeableEquipment(EquipmentData equipmentData) {
        super(equipmentData);
    }

    public void ultimate() {
        setLevel(getLevel() + 1);
        if(isVisible()) {
            setSlot(equipmentData.getDefaultVisual(getLevel()));
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < getEquipmentData().levels.size() && level >= 0) {
            this.level = level;
        } else {
            throw new IllegalArgumentException("Invalid level");
        }
    }

    public Hashtable<String, IEquipmentValue> getCurrentStat() {
        return equipmentData.levels.getLevel(getLevel());
    }

    @Override
    public void setVisibility(boolean isVisible) {
        super.setVisibility(isVisible);
        if(isVisible) {
            setSlot(getEquipmentData().getDefaultVisual(getLevel()));
        } else {
            setSlot(null);
        }
    }
}
