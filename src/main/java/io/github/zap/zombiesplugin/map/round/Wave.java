package io.github.zap.zombiesplugin.map.round;

import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.Hashtable;

public class Wave {
	private final Hashtable<GameDifficulty,Long> delays;
	private final Hashtable<GameDifficulty,ArrayList<MythicMob>> mobs;

	public Wave() {
		this.delays = new Hashtable<>();
		this.mobs = new Hashtable<>();
	}

	public Wave(Hashtable<GameDifficulty,Long> delays, Hashtable<GameDifficulty,ArrayList<MythicMob>> mobs) {
		this.delays = delays;
		this.mobs = mobs;
	}

	public void addDelay(GameDifficulty difficulty, long delay) {
		this.delays.put(difficulty, delay);
	}

	public void addMobs(GameDifficulty difficulty, ArrayList<MythicMob> mobs) {
		this.mobs.put(difficulty, mobs);
	}

	public long getDelay(GameDifficulty difficulty) {
		if(delays.containsKey(difficulty)) {
			return delays.get(difficulty);
		}
		return -1L;
	}


	public Hashtable<GameDifficulty,Long> getDelays() {
		return delays;
	}

	public ArrayList<MythicMob> getMobs(GameDifficulty difficulty) {
		if(mobs.containsKey(difficulty)) {
			return mobs.get(difficulty);
		}
		return null;
	}

	public Hashtable<GameDifficulty,ArrayList<MythicMob>> getMobs() {
		return mobs;
	}
}