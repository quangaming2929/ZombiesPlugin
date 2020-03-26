package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.round.Wave;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;

public class WaveData {
    public long delay;
    public ArrayList<String> mobNames;

    public WaveData() {}

    public WaveData(Wave from) {
        delay = from.getDelay();
        mobNames = new ArrayList<>();

        for(MythicMob mob : from.getMobs()) {
            mobNames.add(mob.getInternalName());
        }
    }
}
