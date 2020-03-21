package io.github.zap.zombiesplugin.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PacketUtils {
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
}
