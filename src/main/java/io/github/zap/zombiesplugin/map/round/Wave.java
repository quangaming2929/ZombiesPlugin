package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.Hashtable;

public class Wave {
	private final Hashtable<GameDifficulty,Long> delay;
	private final Hashtable<GameDifficulty,ArrayList<MythicMob>> mobs;

	public Wave() {
		this.delay = new Hashtable<>();
		this.mobs = new Hashtable<>();
	}

	public void addDifficulty(GameDifficulty difficulty, long delay) {
		this.delay.put(difficulty, delay);
	}

	public void addDifficulty(GameDifficulty difficulty) {
		mobs.put(difficulty, new ArrayList<>());
	}

	public void addMob(GameDifficulty difficulty, MythicMob mob) {
		if(mobs.containsKey(difficulty)) {
			ArrayList<MythicMob> current = mobs.get(difficulty);
			if(current != null) {
				current.add(mob);
			}
		}
	}

	public long getDelay(GameDifficulty difficulty) {
		if(delay.containsKey(difficulty)) {
			return delay.get(difficulty);
		}
		return -1L;
	}

	public ArrayList<MythicMob> getMobs(GameDifficulty difficulty) {
		if(mobs.containsKey(difficulty)) {
			return mobs.get(difficulty);
		}
		return null;
	}

	public Hashtable<GameDifficulty,Long> getDelayMappings() {
		return delay;
	}

	public Hashtable<GameDifficulty,ArrayList<MythicMob>> getMobMappings() {
		return mobs;
	}
}