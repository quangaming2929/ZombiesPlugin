package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.map.round.Wave;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.Hashtable;

public class WaveData {
    public Hashtable<GameDifficulty,Long> delay;
    public Hashtable<GameDifficulty,ArrayList<String>> mobs;

    public WaveData() {}

    public WaveData(Wave from) {
        delay = from.getDelayMappings();
        mobs = new Hashtable<>();

        for(GameDifficulty difficulty : from.getMobMappings().keySet()) {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<MythicMob> mobs = from.getMobs(difficulty);

            for(MythicMob mob : mobs) {
                names.add(mob.getInternalName());
            }

            this.mobs.put(difficulty, names);
        }
    }
}
