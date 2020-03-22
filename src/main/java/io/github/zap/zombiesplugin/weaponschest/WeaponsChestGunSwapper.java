package io.github.zap.zombiesplugin.weaponschest;

import io.github.zap.zombiesplugin.guns.Gun;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;

public class WeaponsChestGunSwapper implements Listener {

	private final Player player;

	private final Chest chest;

	private final List<Gun> weaponsChestWeapons;

	private final ItemStack weaponStack;

	private final Item droppedWeapon;

	private int counter = 0;

	private boolean ableToCollect = false;

	public WeaponsChestGunSwapper(Player player, DoubleChest doubleChest, Chest chest, List<Gun> weaponsChestWeapons) {
		this.player = player;
		this.chest = chest;
		this.weaponsChestWeapons = new ArrayList<>(weaponsChestWeapons);
		Collections.shuffle(weaponsChestWeapons);

		Location doubleChestLocation = doubleChest.getLocation();
		doubleChestLocation.setY(doubleChestLocation.getY() + 11.0/8.0);

		// TODO: Initialize with player's ultimates
		weaponStack = weaponsChestWeapons.get(0).gunData.getDefaultVisual(0, null);

		droppedWeapon = player.getWorld().dropItem(doubleChestLocation, weaponStack);
	}

	public void nextGun() {
		counter++;
		weaponsChestWeapons.get(counter % weaponsChestWeapons.size()).gunData.getDefaultVisual(0, weaponStack);
	}

	public void endSwapping() {
		ableToCollect = true;
		// TODO: Display end of swap
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent event){
		if (droppedWeapon.equals(event.getItem())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK && ableToCollect && event.getPlayer().equals(player)) {
			if (event.getHand() != null) {
				if (event.getHand() == EquipmentSlot.OFF_HAND) return;
			}

			Player player = event.getPlayer();
			Collection<Item> nearbyItems = player.getWorld().getNearbyEntitiesByType(Item.class, player.getEyeLocation(), 3);

			testRayTrace(event, nearbyItems);
		}
	}

	private void testRayTrace(PlayerInteractEvent event, Collection<Item> nearbyItems) {
		for (Item item : nearbyItems) {
			if (item.equals(droppedWeapon)) {
				BoundingBox boundingBox = item.getBoundingBox();

				RayTraceResult result = boundingBox.rayTrace(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection(), 2.0);
				if (result != null) {
					// TODO: Add player gun
					event.getPlayer().getInventory().addItem(item.getItemStack());
				}
			}
		}
	}
}
