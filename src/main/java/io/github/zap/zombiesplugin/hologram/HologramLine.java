package io.github.zap.zombiesplugin.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramLine {

	private ArmorStand armorStand;

	public HologramLine(Location location, String name) {
		armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

		armorStand.setGravity(false);
		armorStand.setCustomNameVisible(true);
		armorStand.setVisible(false);
		setName(name);
	}

	public void remove() {
		if (armorStand != null) {
			armorStand.remove();
		}
	}

	public void setName(String name) {
		if (armorStand != null) {
			armorStand.setCustomName(name);
		}
	}

	public ArmorStand getArmorStand() {
		return armorStand;
	}
}
