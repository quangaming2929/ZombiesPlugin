package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public abstract class ArmorStandShop extends Shop {

	protected final ArmorStand armorStand;

	public ArmorStandShop(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Location hologramLocation, Location armorStandLocation, String lineOne) {
		super(gameManager, requiresPower, cost, purchaseSuccessSound, hologramLocation, lineOne, testCostMessage(requiresPower, cost));
		armorStand = (ArmorStand) armorStandLocation.getWorld().spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);
		armorStand.setGravity(false);
	}

	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		if (event.getRightClicked().equals(armorStand)) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onPurchaseAttempt(PlayerEvent event) {
		if (event instanceof PlayerInteractAtEntityEvent && gameManager.getPlayerManager().getAssociatedUser(event.getPlayer()) != null) {
			Player player = event.getPlayer();
			User user = gameManager.getPlayerManager().getAssociatedUser(player);

			checkCostBeforePurchase(event);
		}
	}
}
