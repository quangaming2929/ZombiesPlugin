package io.github.zap.zombiesplugin.map.round;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;

public class Wave {
	private final long delay;
	private final ArrayList<MythicMob> mobs;

	public Wave(long delay, ArrayList<MythicMob> mobs) {
		this.delay = delay;
		this.mobs = mobs;
	}

	public long getDelay() {
		return delay;
	}

	public ArrayList<MythicMob> getMobs() {
		return mobs;
	}
}