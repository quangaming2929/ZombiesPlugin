package io.github.zap.zombiesplugin.guns;

import io.github.zap.zombiesplugin.hotbar.EquipmentObjectGroup;
import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;

import java.util.Hashtable;

public class GunObjectGroup extends EquipmentObjectGroup {
    private int slotCount = 2;
    protected Hashtable<String, Integer> oldUltimateValues = new Hashtable<>();

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        for (HotbarGroupObject o : objects) {
            if (!(o.object instanceof Gun)) {
                if (o.object != null) {
                    o.object.onRemoved();
                }

                o.object = new GunPlaceHolder(o.itemGroupId + 1);
                o.object.init(o.slotId, player);
            }
        }

        this.slotCount = slotCount;
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        if(target.itemGroupId < getSlotCount()) {
            target.object = new GunPlaceHolder(target.itemGroupId + 1);
            target.object.init(target.slotId, player);
        }
    }

    @Override
    public void addObject(HotbarObject object, int slotId) {
        if ( (object instanceof Gun || object instanceof GunPlaceHolder)) {
            super.addObject(object, slotId);

            if (object instanceof Gun) {
                Gun gun = (Gun) object;
                if(oldUltimateValues.containsKey(gun.gunData.id)) {
                    gun.setLevel(oldUltimateValues.get(gun.gunData.id));
                } else {
                    gun.setLevel(0);
                }
            }
        } else {
            throw new IllegalArgumentException("This group only accept Gun and GunPlaceHolder");
        }
    }
}
