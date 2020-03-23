package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Shop implements Listener {

	protected final GameManager gameManager;

	protected final String rewardName;

	protected int cost;

	protected final Sound purchaseSuccessSound;

	protected final Sound purchaseFailureSound = Sound.ENTITY_ENDERMAN_TELEPORT;

	public Shop(GameManager gameManager, String rewardName, Sound purchaseSuccessSound) {
		this.gameManager = gameManager;
		this.rewardName = rewardName;
		this.purchaseSuccessSound = purchaseSuccessSound;
		ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
	}

	public void playPurchaseSound(Player player, boolean success) {
		Sound sound = (success) ? purchaseSuccessSound : purchaseFailureSound;
		float pitch = (success) ? 2.0F : 0.5F;

		player.playSound(player.getEyeLocation(), sound, 1.0F, pitch);
	}

	@EventHandler
	public abstract void onPurchaseAttempt(PlayerInteractEvent event);

	protected abstract boolean purchase(User user);
}
