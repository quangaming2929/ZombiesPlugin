package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Shop implements Listener {

	private final GameManager gameManager;

	public Shop(GameManager gameManager) {
		this.gameManager = gameManager;
		ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
	}

	public abstract boolean checkPermission(Player player);

	public abstract void buy(Player player);

	@EventHandler
	public abstract void onRightClick(PlayerInteractEvent event);

}
