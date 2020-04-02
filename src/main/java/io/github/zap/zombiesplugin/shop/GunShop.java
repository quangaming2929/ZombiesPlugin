package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class GunShop extends ArmorStandShop {

	private final Item item;

	private final String gunName;

	public GunShop(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Block block, String gunName) {
		super(gameManager, requiresPower, cost, purchaseSuccessSound, block.getLocation().clone().add(0.5, 0.25, 0.5), block.getLocation().clone().add(0.5, -1, 0.5), ChatColor.GREEN + gunName);

		this.gunName = gunName;

		Location itemLocation = block.getLocation().clone().add(0.5, 0.481250, 0.5);
		item = itemLocation.getWorld().dropItem(itemLocation, new ItemStack(Material.WOODEN_HOE)); // TODO: Actually get gun

		item.setVelocity(item.getVelocity().zero());
		item.setGravity(false);
	}

	@Override
	protected boolean purchase(User user) {
		int slot = user.getPlayer().getInventory().getHeldItemSlot();
		if (user.getHotbar().getHotbarObject(2) instanceof GunPlaceHolder) {
		}
		try {
			Gun gun = user.getGunUser().getGunBySlot(slot);
			if (gun != null) {
				// TODO: Refill the gun
			} else {
				// TODO: Purchase the gun
				return false;
			}
		} catch (Exception e) {
			user.getPlayer().sendMessage(ChatColor.RED + "It looks like the server owner didn't set up this gun correctly!");
		}

		return true;
	}

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (event.getItem().equals(item)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		if (event.getEntity().equals(item)) {
			event.setCancelled(true);
		}
	}
}
