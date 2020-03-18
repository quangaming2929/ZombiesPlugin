package io.github.zap.zombiesplugin.hologram;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Hologram {
	
	private final List<HologramLine> hologramLines = new ArrayList<>();

	private final Location location;
	
	private final double lineSpace;

	public Hologram(Location location, double lineSpace) {
		this.location = location;
		this.lineSpace = lineSpace;
	}

	public Hologram addLine(String line) {
		hologramLines.add(new HologramLine(location.clone().add(0, -lineSpace * hologramLines.size(), 0), line));
		return this;
	}

	public Hologram addLine(String line, int index) {
		hologramLines.add(index, new HologramLine(location.clone().add(0, -lineSpace * hologramLines.size(), 0), line));
		return this;
	}

	public Hologram removeLine(int index) {
		hologramLines.get(index).remove();
		hologramLines.remove(index);

		for (int i = index; i < hologramLines.size(); i++) {
			ArmorStand armorStand = hologramLines.get(i).getArmorStand();
			Location location = armorStand.getLocation();

			armorStand.teleport(location.clone().add(0, lineSpace, 0));
		}

		return this;
	}

	public List<HologramLine> getHologramLines() {
		return hologramLines;
	}
}
