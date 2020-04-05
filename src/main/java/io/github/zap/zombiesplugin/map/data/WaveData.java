package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.manager.GameDifficulty;
import io.github.zap.zombiesplugin.map.round.Wave;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.Hashtable;

public class WaveData implements IMapData<Wave> {
    public Hashtable<GameDifficulty,Long> delay;
    public Hashtable<GameDifficulty,ArrayList<String>> mobs;

    public WaveData() {}

    public WaveData(Wave from) {
        delay = from.getDelays();
        mobs = new Hashtable<>();

        for(GameDifficulty difficulty : from.getMobs().keySet()) {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<MythicMob> mobs = from.getMobs(difficulty);

            for(MythicMob mob : mobs) {
                names.add(mob.getInternalName());
            }

            this.mobs.put(difficulty, names);
        }
    }

    @Override
    public Wave load(Object args) {
        Hashtable<GameDifficulty,ArrayList<MythicMob>> result = new Hashtable<>();
        for(GameDifficulty difficulty : mobs.keySet()) {
            ArrayList<MythicMob> resultMobs = new ArrayList<>();
            for(String mobName : mobs.get(difficulty)) {
                resultMobs.add(MythicMobs.inst().getAPIHelper().getMythicMob(mobName));
            }
            result.put(difficulty, resultMobs);
        }

        return new Wave(delay, result);
    }
}
