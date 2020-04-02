package io.github.zap.zombiesplugin.shop.weaponschest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.Gun;
import io.github.zap.zombiesplugin.manager.GameManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.ArmorStandShop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WeaponsChest /*extends ArmorStandShop*/ {

	private final GameManager gameManager;

	private final List<Block> weaponsChestBlocks;

	private final List<Gun> weaponsChestWeapons;

	private final Gun[] weapons;

	public WeaponsChest(GameManager gameManager, List<Block> weaponsChestBlocks, List<Gun> weaponsChestWeapons, Gun[] weapons) {
		// super(gameManager, );
		this.gameManager = gameManager;
		this.weaponsChestBlocks = weaponsChestBlocks;
		this.weaponsChestWeapons = weaponsChestWeapons;
		this.weapons = weapons;
	}

	@EventHandler
	public void onPurchaseAttempt(PlayerEvent event) {
		if (event instanceof PlayerInteractEvent) {
			PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;

			if (interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block block = interactEvent.getClickedBlock();
				Player player = interactEvent.getPlayer();

				if (weaponsChestBlocks.contains(block) && block instanceof Chest) {
					Chest chest = (Chest) block;

					if (chest.getInventory().getHolder() instanceof DoubleChest) {
						DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
						interactEvent.setCancelled(true);
						try {
							// TODO: Check player gold
							ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, changeChest(block, false));
							startRoll(player, doubleChest, chest);
						} catch (InvocationTargetException e) {
							// TODO: I'm not exactly sure how to handle this...would it even ever happen?
						}
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
				gunSwapper.endSwapping();;
			}
		}.runTaskLater(ZombiesPlugin.instance, accumulatedDelay);
	}

	public static PacketContainer changeChest(Block block, boolean open) {
		int openValue = (open) ? 1 : 0;

		Location location = block.getLocation();
		BlockPosition blockPosition = new BlockPosition((int) location.getX(), (int) location.getY(), (int) location.getZ());
		PacketContainer chestOpen = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
		chestOpen.getBlockPositionModifier().write(0, blockPosition);
		chestOpen.getIntegers().write(0, 1);
		chestOpen.getIntegers().write(1, openValue);
		chestOpen.getBlocks().write(0, Material.CHEST);
		return chestOpen;
	}

	/*@Override
	protected boolean purchase(User user) {
		return false;
	}*/
}
