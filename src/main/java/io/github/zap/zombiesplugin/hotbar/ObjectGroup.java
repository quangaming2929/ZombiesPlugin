package io.github.zap.zombiesplugin.hotbar;

import net.sf.cglib.asm.$ClassReader;
import org.bukkit.entity.Player;

import java.beans.Visibility;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a group can be added into hotbar slot. It allow you to manage objects in a contained group
 * and HotbarObjects cannot override your contained object. It also allow you to implement your own way to
 * add Object into the hotbars slot
 */
public abstract class ObjectGroup {
    public String name;
    protected Player player;
    protected Integer[] preservedSlots;
    protected List<HotbarGroupObject> objects;

    public abstract void addObject(HotbarObject object, int slotId);

    public abstract void removeObject(HotbarObject object);

    public void setVisibility(boolean isVisible) {
        for (HotbarGroupObject go : objects) {
            if (go.object != null) {
                go.object.setVisibility(isVisible);
            }
        }
    }

    public void init(Player player, String name, Integer... preservedSlots) {
        this.player = player;
        this.name = name;
        this.preservedSlots = preservedSlots;
        this.objects = new ArrayList<>(preservedSlots.length);

        for (int i = 0; i < preservedSlots.length; i++) {
            objects.add(new HotbarGroupObject(i, preservedSlots[i]));
        }
    }

    public void onRemoved() {
        for (HotbarGroupObject go : objects) {
            if (go.object != null) {
                go.object.onRemoved();
            }
        }
    }

    public boolean isOverlap (Integer... slots) {
        for(Integer outSlot : slots) {
            for (Integer inSlot : preservedSlots ) {
                if (inSlot == outSlot) {
                    return true;
                }
            }
        }

        return false;
    }

    public Integer[] getPreservedSlots() {
        return preservedSlots;
    }

    public List<HotbarObject> getObjects() {
        List<HotbarObject> val = new ArrayList<>();

        for(HotbarGroupObject object : objects) {
            if (object.object != null) {
                val.add(object.object);
            }
        }

        return val;
    }

    public int count() {
        int i = 0;
        for(HotbarGroupObject object : objects) {
            if (object.object != null) {
                i++;
            }
        }

        return  i;
    }

    protected HotbarGroupObject firstEmpty() {
        for (HotbarGroupObject obj : objects) {
            if(obj.object == null) {
                return obj;
            }
        }

        return null;
    }

    protected HotbarGroupObject getByItemSlotId(int id) {
        for (HotbarGroupObject obj : objects) {
            if(obj.slotId == id) {
                return obj;
            }
        }

        return null;
    }

    protected HotbarGroupObject getByHotbarObject(HotbarObject object) {
        for (HotbarGroupObject obj : objects) {
            if(obj.object == object) {
                return  obj;
            }
        }

        return null;
    }
}
