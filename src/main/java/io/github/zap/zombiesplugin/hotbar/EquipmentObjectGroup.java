package io.github.zap.zombiesplugin.hotbar;

import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import org.bukkit.entity.Player;

/**
 * This type of group add object logic similar to how guns and perks added
 * in the original game
 */
public class EquipmentObjectGroup extends ObjectGroup {
    @Override
    public void addObject(HotbarObject object, int slotId) {
        if(isOverlap(slotId)) {
            HotbarGroupObject obj = firstEmpty();
            if (obj == null) {
                obj = getByItemSlotId(slotId);
            }

            if (obj.object != null) {
                object.init(obj.slotId, player);
                obj.object = object;
            } else {
                throw new IllegalArgumentException("This slot isn't unlock yet");
            }
        } else {
            throw new IllegalArgumentException("slotId is not preserved in this group " + name);
        }
    }

    @Override
    public void removeObject(HotbarObject object) {
        HotbarGroupObject groupObject = getByHotbarObject(object);
        if(groupObject != null) {
            object.onRemoved();
            addPlaceHolder(groupObject);
        } else {
            throw new IllegalArgumentException("object not present in this group " + name);
        }

    }

    @Override
    public void init(Player player, String name, Integer... preservedSlots) {
        super.init(player, name, preservedSlots);

        for (HotbarGroupObject go : objects) {
            addPlaceHolder(go);
        }
    }

    /**
     * Override this method to add your own placeholder
     * @param target
     */
    public void addPlaceHolder(HotbarGroupObject target ) {
        target.object = null;
    }

    @Override
    protected HotbarGroupObject firstEmpty() {
        for (HotbarGroupObject go : objects) {
            if (go.object instanceof GunPlaceHolder) {
                return go;
            }
        }

        return null;
    }

    public boolean isEquipableAt(int slotId) {
        if (firstEmpty() != null) {
            return true;
        } else {
            HotbarGroupObject go = getByItemSlotId(slotId);
            return go != null && go.object != null;
        }
    }
}
