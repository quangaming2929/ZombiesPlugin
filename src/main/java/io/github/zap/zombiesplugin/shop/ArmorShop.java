package io.github.zap.zombiesplugin.shop;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.guns.data.SoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ArmorShop extends ArmorStandShop {

	private final ItemStack[] armor;

	private boolean active = true;

	public ArmorShop(GameManager gameManager, boolean requiresPower, int cost, SoundFx purchaseSuccessSound, Location hologramLocation, Location armorStandLocation, String armorName, ItemStack[] armor) {
		super(gameManager, requiresPower, cost, purchaseSuccessSound, hologramLocation, armorStandLocation, ChatColor.GREEN + armorName);
		this.armor = armor;
	}

	public void notifyArmorChange(Player player) {
		ItemStack[] playerArmor = player.getInventory().getArmorContents();
		ItemStack[] newArmorStandArmor = getNewArmor(player);

		if (Arrays.stream(playerArmor).anyMatch(playerItemStack -> playerItemStack != null && Arrays.stream(armor).anyMatch(armorItemStack -> armorItemStack != null && playerItemStack.getType().getMaxDurability() > armorItemStack.getType().getMaxDurability()))) {
			editLine(ChatColor.GOLD + "UNLOCKED", 1);
			this.active = false;
		}

		sendEntityEquipmentPacket(newArmorStandArmor, player);
	}

	private ItemStack[] getNewArmor(Player player) {
		ItemStack[] playerArmor = player.getInventory().getArmorContents();
		ItemStack[] newArmorStandArmor = new ItemStack[4];

		for (int i = 0; i < playerArmor.length; i++) {
			if (armor[i] != null) {
				newArmorStandArmor[i] = armor[i];
			} else {
				newArmorStandArmor[i] = playerArmor[i];
			}
		}
		return newArmorStandArmor;
	}

	private void sendEntityEquipmentPacket(ItemStack[] newArmorStandArmor, Player player) {

		final Map<Integer, EnumWrappers.ItemSlot> integerToItemSlotMap = new HashMap<>();

		integerToItemSlotMap.put(0, EnumWrappers.ItemSlot.FEET);
		integerToItemSlotMap.put(1, EnumWrappers.ItemSlot.LEGS);
		integerToItemSlotMap.put(2, EnumWrappers.ItemSlot.CHEST);
		integerToItemSlotMap.put(3, EnumWrappers.ItemSlot.HEAD);

		for (int i = 0; i < 4; i++) {
			PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
			packetContainer.getIntegers().write(0, armorStand.getEntityId());
			packetContainer.getItemSlots().write(0, integerToItemSlotMap.get(i));
			packetContainer.getItemModifier().write(0, newArmorStandArmor[i]);

			try {
				ZombiesPlugin.instance.getProtocolManager().sendServerPacket(player, packetContainer);
			} catch (InvocationTargetException exception) {
				player.sendMessage("An error occurred when you purchased that armor!");
				break;
			}
		}
	}

	@Override
	protected boolean purchase(User user) {
		if (!active) {
			user.getPlayer().sendMessage(ChatColor.RED + "You have already unlocked this armor!");
			return false;
		} else {
			Objects.requireNonNull(user.getPlayer().getEquipment()).setArmorContents(getNewArmor(user.getPlayer()));

			for (Shop shop : gameManager.getMap().getShops()) {
				if (shop instanceof ArmorShop) {
					((ArmorShop) shop).notifyArmorChange(user.getPlayer());
				}
			}

			return true;
		}
	}

	@Override
	public void display() {
		super.display();
		EntityEquipment equipment = armorStand.getEquipment();
		equipment.setArmorContents(armor);
		armorStand.setSmall(true);
		armorStand.setVisible(false);
	}
}
