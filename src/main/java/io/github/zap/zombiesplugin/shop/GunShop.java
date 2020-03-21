package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.guns.GunPlaceHolder;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class GunShop extends WallShop {

	public GunShop(GameManager gameManager, String rewardName, int cost, Location location) {
		super(gameManager, rewardName, cost, location.getWorld().dropItem(location, new ItemStack(Material.WOODEN_HOE))); // TODO: Actually get gun
		Item item = (Item) entity;

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
			}

			return true;
		} catch (Exception e) {
			user.getPlayer().sendMessage(ChatColor.RED + "It looks like the server owner didn't set up this gun correctly!");
		}

		return false;
	}

	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		if (event.getItem().equals(entity)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		if (event.getEntity().equals(entity)) {
			event.setCancelled(true);
		}
	}
}
