package io.github.zap.zombiesplugin.mob;

import org.bukkit.entity.EntityType;

public abstract class Mob {
	private MobInfo info;

	public Mob(MobInfo info) {
		this.info = info;
	}

	public MobInfo getMobInfo() { return info; }
}
