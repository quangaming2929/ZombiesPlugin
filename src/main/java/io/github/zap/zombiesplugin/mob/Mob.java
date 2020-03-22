package io.github.zap.zombiesplugin.mob;

public abstract class Mob {
	private MobInfo info;

	public Mob(MobInfo info) {
		this.info = info;
	}

	public MobInfo getMobInfo() { return info; }
}
