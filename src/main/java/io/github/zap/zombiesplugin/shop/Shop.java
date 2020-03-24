package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.hologram.Hologram;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

public abstract class Shop implements Listener {

	protected final GameManager gameManager;

	private final Hologram hologram;

	private final boolean requiresPower;

	protected int cost;

	protected final SoundFx purchaseSuccessSound;

	protected final SoundFx purchaseFailureSound = new SoundFx(Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.5F);

	protected boolean turnedOn = false;

	protected Shop(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Location hologramLocation, String lineOne, String lineTwo) {
		this.gameManager = gameManager;
		this.requiresPower = requiresPower;
		this.cost = cost;
		this.purchaseSuccessSound = purchaseSuccessSound;
		hologram = new Hologram(hologramLocation,0.25);
		hologram.addLine(lineOne);
		hologram.addLine(lineTwo);

		ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
	}

	public void playPurchaseSound(Player player, Boolean success) {
		if (success != null) {
			SoundFx sound = (success) ? purchaseSuccessSound : purchaseFailureSound;
			player.playSound(player.getEyeLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
		}
	}

	public void editLine(String line, int index) {
		hologram.editLine(line, index);
	}

	protected void setCost(int cost) {
		this.cost = cost;
		editLine(String.format(ChatColor.GOLD + "&x Gold", cost), 1);
	}

	protected static String testCostMessage(boolean requiresPower, int cost) {
		if (requiresPower) {
			return ChatColor.GRAY + "" + ChatColor.ITALIC + "Requires Power!";
		} else {
			return ChatColor.GOLD + String.valueOf(cost) + " Gold";
		}
	}

	protected void checkCostBeforePurchase(PlayerEvent event) {
		User user = gameManager.getPlayerManager().getAssociatedUser(event.getPlayer());
		if (user.getGold() < cost) {
			user.getPlayer().sendMessage(ChatColor.RED + "You don't have enough gold!");
		} else {
			playPurchaseSound(user.getPlayer(), purchase(user));
		}
	}

	public boolean doesRequirePower() {
		return requiresPower;
	}

	public void turnOn() {
		turnedOn = true;
		editLine(getDefaultCostLine(), 1);
	}

	protected String getDefaultCostLine() {
		return ChatColor.GOLD + String.valueOf(cost) + " Gold";
	}

	@EventHandler
	public abstract void onPurchaseAttempt(PlayerEvent event);

	protected abstract boolean purchase(User user);

}
