package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.hologram.Hologram;
import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.Location;
import org.bukkit.Sound;

public abstract class SingleHologramShop extends Shop {
	protected final Hologram hologram;

	public SingleHologramShop(GameManager gameManager, String rewardName, Sound purchaseSuccessSound, Location location, String lineOne, String lineTwo) {
		super(gameManager, rewardName, purchaseSuccessSound);
		hologram = new Hologram(location,0.25);
		hologram.addLine(lineOne);
		hologram.addLine(lineTwo);
	}

	public void editLine(String line, int index) {
		hologram.editLine(line, index);
	}
}
