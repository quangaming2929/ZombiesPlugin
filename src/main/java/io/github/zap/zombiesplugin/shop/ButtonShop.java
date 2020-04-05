package io.github.zap.zombiesplugin.shop;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public abstract class ButtonShop extends SingleHologramShop {

	protected Block button;

	public ButtonShop(GameManager gameManager, String rewardName, Sound purchaseSuccessSound, Block button, Location hologramLocation) {
		super(gameManager, rewardName, purchaseSuccessSound, hologramLocation, ChatColor.BLUE + "" + ChatColor.BOLD + rewardName, ChatColor.GRAY + "" + ChatColor.ITALIC + "Requires Power!"); // TODO: Find these values
		this.button = button;
		if (!blockIsButton()) {
			throw new IllegalArgumentException("Block button must be a block of type button!");
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK && gameManager.getUserManager().getAssociatedUser(event.getPlayer()) != null && event.getClickedBlock() != null) {
			if (event.getClickedBlock().equals(button)) {
				User user = gameManager.getUserManager().getAssociatedUser(event.getPlayer());
				if (user.getGold() < cost) {
					user.getPlayer().sendMessage(ChatColor.RED + "You don't have enough gold!");
				} else {
					purchase(user);
				}
			}
		}
	}

	private boolean blockIsButton() {
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
