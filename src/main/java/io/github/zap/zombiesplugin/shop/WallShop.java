package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class WallShop extends Shop {

	protected final String name;

	protected int cost;

	protected final Entity entity;

	protected final Hologram hologram;

	public WallShop(String name, int cost, Entity entity) {
		this.name = name;
		this.cost = cost;
		this.entity = entity;

		this.hologram = new Hologram(entity.getLocation().clone().subtract(0, -1, 0), 0.25); // TODO: Verify these values
		hologram.addLine(String.format("&a%s", name));
		hologram.addLine(String.format("&6%x Gold", cost));
	}

	public void setCostLine(String line) {
		hologram.editLine(line, 1);
	}

	@Override
	public boolean checkPermission(Player player) {
		// TODO: Implementation
		return true; // Placeholder
	}

	public void buy(Player player) {
		// TODO: Implementation
	}

	@Override
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.HAND) { // TODO: Check other things
			Player player = event.getPlayer();
			Location location = player.getLocation();

			if (entity.getBoundingBox().rayTrace(location.toVector(), location.getDirection(), 3.0) != null) {
				buy(event.getPlayer());
			}
		}
	}

}
