package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class TeamMachine extends ButtonShop {
	public TeamMachine(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Block button, Location hologramLocation, String perkName) {
		super(gameManager, requiresPower, cost, purchaseSuccessSound, button, hologramLocation, perkName);
	}

	@Override
	protected boolean purchase(User user) {
		return false;
	}
}
