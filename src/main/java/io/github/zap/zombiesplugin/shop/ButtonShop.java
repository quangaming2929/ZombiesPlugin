package io.github.zap.zombiesplugin.shop;

import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public abstract class ButtonShop extends Shop {

	protected Block button;

	public ButtonShop(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Block button, Location hologramLocation, String perkName) {
		super(gameManager, requiresPower, cost, purchaseSuccessSound, hologramLocation, ChatColor.BLUE + "" + ChatColor.BOLD + perkName, testCostMessage(requiresPower, cost)); // TODO: Find these values
		this.button = button;
		if (!blockIsButtonOrLever()) {
			throw new IllegalArgumentException("Block button must be a block of type button or a lever!");
		}
	}

	@Override
	public void onPurchaseAttempt(PlayerEvent event) {
		if (event instanceof PlayerInteractEvent) {
			PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;

			if (interactEvent.getHand() == EquipmentSlot.HAND && interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK && gameManager.getPlayerManager().getAssociatedUser(event.getPlayer()) != null && interactEvent.getClickedBlock() != null) {
				if (interactEvent.getClickedBlock().equals(button)) {
					checkCostBeforePurchase(event);
				}
			}
		}
	}

	private boolean blockIsButtonOrLever() {
		List<Material> buttons = new ArrayList<>();
		buttons.add(Material.STONE_BUTTON);
		buttons.add(Material.OAK_BUTTON);
		buttons.add(Material.BIRCH_BUTTON);
		buttons.add(Material.SPRUCE_BUTTON);
		buttons.add(Material.DARK_OAK_BUTTON);
		buttons.add(Material.JUNGLE_BUTTON);
		buttons.add(Material.ACACIA_BUTTON);

		return buttons.stream().anyMatch(material -> material == button.getType());
	}
}
