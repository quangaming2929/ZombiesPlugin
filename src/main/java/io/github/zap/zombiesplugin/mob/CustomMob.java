package io.github.zap.zombiesplugin.mob;

import org.bukkit.inventory.ItemStack;

public abstract class CustomMob {
	private MobInfo info;

	private final ItemStack helmet;
	private final ItemStack chestplate;
	private final ItemStack leggings;
	private final ItemStack boots;

	public CustomMob(MobInfo info, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
		this.info = info;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}

	public MobInfo getMobInfo() { return info; }
}
