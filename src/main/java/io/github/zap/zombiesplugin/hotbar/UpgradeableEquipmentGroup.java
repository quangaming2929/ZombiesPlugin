package io.github.zap.zombiesplugin.hotbar;

import io.github.zap.zombiesplugin.equipments.UpgradeableEquipment;
import io.github.zap.zombiesplugin.placeholder.EquipmentPlaceHolder;

import java.util.Hashtable;

public class UpgradeableEquipmentGroup extends EquipmentObjectGroup {
    protected Hashtable<String, Integer> oldUltimateValues = new Hashtable<>();
    protected final boolean ignoreLevel;

    public UpgradeableEquipmentGroup(boolean ignoreLevel) {
        this.ignoreLevel = ignoreLevel;
    }

    @Override
    public void addObject(HotbarObject object, int slotId) {
        if ((object instanceof UpgradeableEquipment || object instanceof EquipmentPlaceHolder)) {
            super.addObject(object, slotId);

            if (object instanceof UpgradeableEquipment && !ignoreLevel) {
                UpgradeableEquipment upgradeable = (UpgradeableEquipment) object;
                String id = upgradeable.getEquipmentData().id;
                if(oldUltimateValues.containsKey(id)) {
                    upgradeable.setLevel(oldUltimateValues.get(id));
                } else {
                    upgradeable.setLevel(0);
                }
            }
        } else {
            throw new IllegalArgumentException("This group only accept Gun and GunPlaceHolder");
        }
    }

    @Override
    public void removeObject(HotbarObject object) {
        super.removeObject(object);
        if (object instanceof UpgradeableEquipment && !ignoreLevel) {
            // Save the equipment ultimate value
            UpgradeableEquipment upgradeable = (UpgradeableEquipment) object;
            String id = upgradeable.getEquipmentData().id;
            int value = upgradeable.getLevel();
            if (oldUltimateValues.containsKey(id)) {
                oldUltimateValues.replace(id, value);
            } else {
                oldUltimateValues.put(id, value);
            }
        }
    }
}
