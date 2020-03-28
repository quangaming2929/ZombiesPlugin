package io.github.zap.zombiesplugin.equipments.guns;

import io.github.zap.zombiesplugin.events.GunEquippedEventArgs;
import io.github.zap.zombiesplugin.events.GunUnEquippedEventArgs;
import io.github.zap.zombiesplugin.hotbar.HotbarGroupObject;
import io.github.zap.zombiesplugin.hotbar.UpgradeableEquipmentGroup;
import io.github.zap.zombiesplugin.equipments.EquipmentPlaceHolder;
import io.github.zap.zombiesplugin.events.EventHandler;


import java.util.Hashtable;

public class GunObjectGroup extends UpgradeableEquipmentGroup {
    private int slotCount = 2;
    protected Hashtable<String, Integer> oldUltimateValues = new Hashtable<>();

    public GunObjectGroup(boolean ignoreLevel) {
        super(ignoreLevel);
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        for (HotbarGroupObject o : objects) {
            if (!(o.object instanceof Gun)) {
                if (o.object != null) {
                    o.object.onRemoved();
                }

                o.object = EquipmentPlaceHolder.createGunPlaceHolder(o.itemGroupId + 1);
                o.object.init(o.slotId, player, player.getInventory().getHeldItemSlot() == o.slotId, true);
            }
        }

        this.slotCount = slotCount;
    }

    @Override
    public void addPlaceHolder(HotbarGroupObject target) {
        if(target.itemGroupId < getSlotCount()) {
            target.object = EquipmentPlaceHolder.createGunPlaceHolder(target.itemGroupId + 1);
            target.object.init(target.slotId, player, player.getInventory().getHeldItemSlot() == target.slotId, true);
        }
    }



    public final EventHandler<GunEquippedEventArgs> gunEquipped = new EventHandler<>();
    public final EventHandler<GunUnEquippedEventArgs> gunUnEquipped = new EventHandler<>();

    protected void onGunEquipped (Gun gun) {
        gunEquipped.invoke(this, new GunEquippedEventArgs(gun));
    }

    protected void onGunUnEquipped (Gun gun) {
        gunUnEquipped.invoke(this, new GunUnEquippedEventArgs(gun));
    }
}
