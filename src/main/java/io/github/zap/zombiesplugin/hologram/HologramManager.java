package io.github.zap.zombiesplugin.hologram;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class HologramManager implements Listener {

	private static HologramManager instance = new HologramManager();

	private static List<Hologram> holograms = new ArrayList<>();

	private HologramManager() {

	}

	public Hologram createHologram(Location location, double lineSpace) {
		Hologram hologram = new Hologram(location, lineSpace);
		holograms.add(hologram);
		return hologram;
	}

	public void removeHologram(Hologram hologram) {
		for (HologramLine hologramLine : hologram.getHologramLines()) {
			hologramLine.remove();
		}

		holograms.remove(hologram);
	}

	public HologramManager getHologramManager() {
		return instance;
	}

	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		List<ArmorStand> hologramArmorStands = new ArrayList<>();

		for (Hologram hologram : holograms) {
			for (HologramLine hologramLine : hologram.getHologramLines()) {
				hologramArmorStands.add(hologramLine.getArmorStand());
			}
		}

		if (hologramArmorStands.contains(event.getRightClicked())) {
			event.setCancelled(true);
		}
	}
}
