package io.github.zap.zombiesplugin.round;

import io.github.zap.zombiesplugin.mob.Mob;
import java.util.List;

public class Wave {

	/**
	 * The delay at the beginning of the wave measured in ticks
	 */
	private final long delay;

	/**
	 * The mobs to spawn
	 */
	private final List<Mob> mobs;

	public Wave(long delay, List<Mob> mobs) {
		this.delay = delay;
		this.mobs = mobs;
	}

	/** Gets the delay of the beginning of the wave
	 *
	 * @return The delay
	 */
	public long getDelay() {
		return delay;
	}

	/** Gets the mobs in the wave
	 *
	 * @return The mobs
	 */
	public List<Mob> getMobs() {
		return mobs;
	}
}
