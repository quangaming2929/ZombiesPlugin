package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public abstract class WallShop extends SingleHologramShop {

	protected final Entity entity;

	public WallShop(GameManager gameManager, String rewardName, int cost, Entity entity) {
		super(gameManager, rewardName, Sound.BLOCK_NOTE_BLOCK_PLING, entity.getLocation().clone().subtract(0, -1, 0), ChatColor.GREEN + rewardName, ChatColor.GOLD + String.valueOf(cost) + "Gold");
		this.cost = cost;
		this.entity = entity;
	}

	protected void setCost(int cost) {
		this.cost = cost;
		editLine(String.format(ChatColor.GOLD + "&x Gold", cost), 1);
	}

	@Override
	public void onPurchaseAttempt(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.HAND && gameManager.getUserManager().getAssociatedUser(event.getPlayer()) != null) { // TODO: Check other things
			Player player = event.getPlayer();
			User user = gameManager.getUserManager().getAssociatedUser(player);
			Location location = player.getEyeLocation();

			if (entity.getBoundingBox().rayTrace(location.toVector(), location.getDirection(), 3.0) != null) {
				if (user.getGold() < cost) {
					user.getPlayer().sendMessage(ChatColor.RED + "You don't have enough gold!");
				} else {
					playPurchaseSound(player, purchase(user));
				}
			}
		}
	}
}
