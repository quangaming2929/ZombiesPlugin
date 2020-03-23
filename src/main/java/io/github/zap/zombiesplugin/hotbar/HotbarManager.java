package io.github.zap.zombiesplugin.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * A helper class to manage hotbar
 */
public class HotbarManager {
    protected String currentProfileName;
    protected List<HotbarObject> currentProfile;
    protected final Hashtable<String, List<HotbarObject>> profiles;
    protected final Player player;

    public HotbarManager(Player player) {
        this.player = player;
        this.profiles = new Hashtable<>();
        this.switchProfile("Default");
    }

    /**
     * Add a item stack into the hotbar, this item will be wrap in UnmanagedHotbarObject
     * Old object will be replaced
     * @param item the item to add
     * @param slot the slotID to add
     */
    public void addObject(ItemStack item, int slot) {
        UnmanagedHotbarObject obj = new UnmanagedHotbarObject(item);
        addObject(obj, slot);
    }

    /**
     * Add a HotbarObject into the hotbar. Old object will be replaced
     * @param object the HotbarObject to add
     * @param slot the slotID to add
     */
    public void addObject(HotbarObject object, int slot) {
        for (HotbarObject obj : currentProfile) {
            if(obj.getSlotID() == slot){
                removeObject(obj);
                break;
            }
        }

        object.init(slot, player);
        currentProfile.add(object);
    }

    /**
     * remove object from the hotbar
     * @param object the object to remove
     */
    public void removeObject(HotbarObject object) {
        object.onRemoved();
        currentProfile.remove(object);
    }

    /**
     * Get current objects in the hotbar. Note: the collection can't be modified
     * @return The read-only collection contains all objects in the current hotbar
     */
    public List<HotbarObject> getObjects() {
        return Collections.unmodifiableList(currentProfile);
    }

    /**
     * Get the current profile name
     */
    public String getCurrentProfileName() {
        return currentProfileName;
    }

    /**
     * Get the hotbar object in the current profile
     * @param slotId the slotID
     */
    public HotbarObject getHotbarObject(int slotId) {
        return getHotbarObject(slotId, getCurrentProfileName());
    }

    /**
     * Get the hotbar object
     * @param slotId the slotID
     * @param profileName the profile to search objects
     */
    public HotbarObject getHotbarObject(int slotId, String profileName) {
        List<HotbarObject> obj = profiles.get(profileName);
        if(obj != null && slotId < obj.size()) {
            for (HotbarObject o : obj) {
                if (o.getSlotID() == slotId) {
                    return  o;
                }
            }
        }

        return null;
    }

    /**
     * switch to another hotbar profile
     * @param name the profile name, new one will be added if the profile name is not exist
     */
    public void switchProfile(String name) {
        // Create new profile if it not exist
        if(!profiles.containsKey(name)) {
            profiles.put(name, new ArrayList<>());
        }

        if(currentProfile != null) {
            for (HotbarObject obj : currentProfile) {
                obj.setVisibility(false);
            }
        }

        currentProfile = profiles.get(name);
        currentProfileName = name;

        for (HotbarObject obj : currentProfile) {
            obj.setVisibility(true);
        }
    }

    /**
     * Process the event for hotbar
     */
    public void processEvent(PlayerInteractEvent event) {
        Action act = event.getAction();
        ItemStack selectedItem = event.getPlayer().getInventory().getItemInMainHand();
        HotbarObject associatedObject = getAssociatedObject(selectedItem);

        if(associatedObject != null) {
            if(act == Action.LEFT_CLICK_AIR || act == Action.LEFT_CLICK_BLOCK) {
                associatedObject.onLeftClick(event.getClickedBlock(), event.getBlockFace());
            } else if (act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK) {
                associatedObject.onRightClick(event.getClickedBlock(), event.getBlockFace());
            }
        }
    }

    /**
     * Process the event for hotbar
     */
    public void processEvent(PlayerItemHeldEvent event) {
        int preSlot = event.getPreviousSlot();
        HotbarObject preObj = getHotbarObject(preSlot);
        int newSlot = event.getNewSlot();
        HotbarObject newObj = getHotbarObject(newSlot);

        boolean isCanceled = false;
        if (preObj != null) {
            isCanceled = preObj.onSlotDeSelected();
        }
        event.setCancelled(isCanceled);
        if (newObj != null && !isCanceled) {
            newObj.onSlotSelected();
        }
    }

    private HotbarObject getAssociatedObject(ItemStack item) {
        for (HotbarObject o : currentProfile) {
            if (o.getSlot() == item) {
                return o;
            }
        }
        return null;
    }
}
