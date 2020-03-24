package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PowerSwitch extends ButtonShop {

	public PowerSwitch(GameManager gameManager, int cost, SoundFx purchaseSuccessSound, Block button, Location hologramLocation, String perkName) {
		super(gameManager, false, cost, purchaseSuccessSound, button, hologramLocation, perkName);
	}

	@Override
	protected boolean purchase(User user) {
		gameManager.turnOnPower();
		return true;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (turnedOn && event.getClickedBlock() != null) {
			if (event.getClickedBlock().equals(button)) {
				event.setCancelled(true);
			}
		}
	}
}
