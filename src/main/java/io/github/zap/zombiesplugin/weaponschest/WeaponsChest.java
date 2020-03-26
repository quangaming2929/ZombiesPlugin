package io.github.zap.zombiesplugin.weaponschest;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.utils.PacketUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WeaponsChest implements Listener {

	private final GameManager gameManager;

	private final List<Block> weaponsChestBlocks;

	private final List<Gun> weaponsChestWeapons;

	private final Gun[] weapons;

	public WeaponsChest(GameManager gameManager, List<Block> weaponsChestBlocks, List<Gun> weaponsChestWeapons, Gun[] weapons) {
		this.gameManager = gameManager;
		this.weaponsChestBlocks = weaponsChestBlocks;
		this.weaponsChestWeapons = weaponsChestWeapons;
		this.weapons = weapons;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();

			if (weaponsChestBlocks.contains(block) && block instanceof Chest) {
				Chest chest = (Chest) block;

				if (chest.getInventory().getHolder() instanceof DoubleChest) {
					DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
					event.setCancelled(true);
					try {
						// TODO: Check player gold
						ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, PacketUtils.changeChest(block, false));
						startRoll(player, doubleChest, chest);
					} catch (InvocationTargetException e) {
						// TODO: I'm not exactly sure how to handle this...would it even ever happen?
					}
				}
			}
		}
	}

	private void startRoll(Player player, DoubleChest doubleChest, Chest chest) {
		WeaponsChestGunSwapper gunSwapper = new WeaponsChestGunSwapper(player, doubleChest, chest, weaponsChestWeapons);
		playJingle(new long[]{5L, 6L, 8L, 10L, 13L}, player, gunSwapper);
	}

	private void playJingle(long[] delays, Player player, WeaponsChestGunSwapper gunSwapper) {
		long accumulatedDelay = 0L;

		for (long delay : delays) {
			Jingle jingle = new Jingle(player, gunSwapper);
			jingle.runTaskTimer(ZombiesPlugin.instance, accumulatedDelay, delay);
			accumulatedDelay += 4 * delay;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				gunSwapper.endSwapping();
			}
		}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
	}

}
