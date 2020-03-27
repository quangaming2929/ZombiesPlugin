package io.github.zap.zombiesplugin.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A helper class to manage hotbar
 */
public class HotbarManager {
    protected String currentProfileName;
    protected HotbarProfile currentProfile;
    protected final Hashtable<String, HotbarProfile> profiles;
    public final Player player;

    public HotbarManager(Player player) {
        this.player = player;
        this.profiles = new Hashtable<>();
        this.switchProfile("Default");
    }

    /**
     * Get the HotbarObject the player is holding on their main hand
     */
    public HotbarObject getSelectedObject() {
        return getHotbarObject(player.getInventory().getHeldItemSlot());
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
        if (isOverlap(slot)) {
            HotbarObject obj = getHotbarObject(slot);
            if (obj != null) {
                removeObject(obj);
            }

            object.init(slot, player, player.getInventory().getHeldItemSlot() == slot, true);
            currentProfile.objects.add(object);
        } else {
            throw new IllegalArgumentException("This slot is overlap with a group");
        }
    }

    /**
     * remove object from the hotbar
     * @param object the object to remove
     */
    public void removeObject(HotbarObject object) {
        if(currentProfile.objects.contains(object)) {
            object.onRemoved();
            currentProfile.objects.remove(object);
        } else {
            throw new IllegalArgumentException("object could not be found in this hotbar profile");
        }

    }

    /**
     * Get current objects in the hotbar includes all groups. Note: the collection can't be modified
     * @return The read-only collection contains all objects in the current hotbar
     */
    public List<HotbarObject> getObjects() {
        List<HotbarObject> objs = new ArrayList<>();
        for (HotbarObject obj : currentProfile.objects) {
            objs.add(obj);
        }

        for (ObjectGroup gr : currentProfile.groups.values()) {
            for (HotbarGroupObject hgo : gr.objects) {
                if (hgo.object != null) {
                   objs.add(hgo.object);
                }
            }
        }

        return objs;
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
        // Objects
        List<HotbarObject> obj = profiles.get(profileName).objects;
        if(obj != null) {
            for (HotbarObject o : obj) {
                if (o.getSlotID() == slotId) {
                    return  o;
                }
            }
        }

        // Groups
        Hashtable<String, ObjectGroup> groups = profiles.get(profileName).groups;
        if(groups != null) {
            for (Map.Entry<String, ObjectGroup> gr : groups.entrySet()) {
                for (HotbarGroupObject groupObj : gr.getValue().objects) {
                    HotbarObject hotbarObj = groupObj.object;
                    if(hotbarObj != null && hotbarObj.getSlotID() == slotId) {
                        return hotbarObj;
                    }
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
            profiles.put(name, new HotbarProfile());
        }

        if(currentProfile != null) {
            // Objects
            for (HotbarObject obj : currentProfile.objects) {
                obj.setVisibility(false);
            }

            // Groups
            for (ObjectGroup gr : currentProfile.groups.values()) {
                gr.setVisibility(false);
            }
        }

        currentProfile = profiles.get(name);
        currentProfileName = name;

        // Objects
        for (HotbarObject obj : currentProfile.objects) {
            obj.setVisibility(true);
        }

        // Groups
        for (ObjectGroup gr : currentProfile.groups.values()) {
            gr.setVisibility(true);
        }
    }

    /**
     * Process the event for hotbar
     */
    public void processEvent(PlayerInteractEvent event) {
        Action act = event.getAction();
        HotbarObject associatedObject = getSelectedObject();

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

    /**
     * Please don't modify anything about ObjectGroup before using this method. This method checks some condition before
     * adding the group into hotbar
     * Example usage:
     * GunObjectGroup gunGroup = new GunObjectGroup();
     * HotbarManager.addGroup("GunGroup", gunGroup, 2,3,4);
     * then you can start modify your instance
     * @param name the group name
     */
    public void addGroup(String name, ObjectGroup group, Integer... preservedSlots) {
        for (ObjectGroup gr : currentProfile.groups.values()) {
            if (gr.isOverlap(preservedSlots)) {
                throw new UnsupportedOperationException("This group overlap with other ObjectGroup(s) in the current profile");
            }
        }

        group.init(player, name, preservedSlots);
        currentProfile.groups.put(name, group);
    }

    public void removeGroup(String name) {
        if (currentProfile.groups.containsKey(name)) {
            currentProfile.groups.get(name).onRemoved();
            currentProfile.groups.remove(name);
        } else {
            throw new IllegalArgumentException("Can't find the provided group");
        }
    }

    public ObjectGroup getGroup(String name) {
        if(currentProfile.groups.containsKey(name)) {
            return currentProfile.groups.get(name);
        }

        return null;
    }

    public boolean isOverlap(Integer... slotId) {
        for (ObjectGroup gr : currentProfile.groups.values()) {
            if(gr.isOverlap(slotId)) {
                return  true;
            }
        }

        return false;
    }

}
