package io.github.zap.zombiesplugin.hotbar;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class HotbarObject {
    private boolean isVisible;

    private Player player;
    private int slotID;
    private boolean isRemoved;
    private boolean isSelected;

    /**
     * Get the associated player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * get the inventory slot count
     */
    public int getSlotID() {
        return slotID;
    }

    /**
     * Called when this slot is selected by the main head
     */
    public void onSlotSelected() { isSelected = true; }

    /**
     * Called when this slot is no longer selected by the main head
     * @return cancel the event
     */
    public boolean onSlotDeSelected() {
        isSelected = false;
        return false;
    }

    /**
     * Return the select state of this hotbar object
     */
    public boolean isSelected() {
        return isSelected;
    }

    //TODO: We might switch the actual main hand here
    /**
     * Set the selection state of this object
     * @param selected
     */
    public void setSelectionState(boolean selected) {
        isSelected = selected;
    }

    /**
     * Called when the player right click using this item
     * @param clickedBlock the block was clicked by the player, returned by PlayerInteractionEvent.getClickedBlock()
     * @param clickedFace the face was clicked  by the player, returned by PlayerInteractionEvent.getBlockFace()
     * @return cancel this event
     */
    public boolean onRightClick(Block clickedBlock, BlockFace clickedFace) { return true; }

    /**
     * Called when the player left click using this item
     * @param clickedBlock the block was clicked by the player, returned by PlayerInteractionEvent.getClickedBlock()
     * @param clickedFace the face was clicked  by the player, returned by PlayerInteractionEvent.getBlockFace()
     * @return cancel this event
     */
    public boolean onLeftClick(Block clickedBlock, BlockFace clickedFace) { return true; }

    /**
     * Get the item slot associate with this object. This methods
     * also ensures that the return value will not null if it not been removed
     * @return
     */
    public ItemStack getSlot() {
        if (!isRemoved) {
            ItemStack i = player.getInventory().getItem(slotID);
            if(i == null) {
                i = new ItemStack(Material.BARRIER, 1);
                player.getInventory().setItem(getSlotID(), i);
            }
            return i;
        } else {
            return null;
        }

    }

    /**
     * Set the new ItemStack to the hotbar object
     * @param itemStack The ItemStack to set
     */
    public void setSlot(ItemStack itemStack) {
        if (!isRemoved) {
            getPlayer().getInventory().setItem(getSlotID(), itemStack);
        }
    }

    /**
     * Called when this object is removed from the hotbar system
     */
    public void onRemoved() {
        if(!isRemoved) {
            setSlot(null);
        }
    }

    /**
     * Change the visibility of this object
     */
    public void setVisibility(boolean isVisible){
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Called when the item is added
     * @param  slot the object reserved slot
     * @param  player the player owns this hotbar
     */
    public void init(int slot, Player player) {
        this.player = player;
        this.slotID = slot;
        this.setVisibility(true);
    }

    /**
     * Called when player try to throw this item, can be cancelled
     * @return if this item allow throw
     */
    public boolean onThrow() {
        return false;
    }
}
