package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.round.Wave;

import java.util.ArrayList;

public class RoundData {
    public String mapName;
    public ArrayList<WaveData> waves;

    public RoundData() {}

    public RoundData(Round from) {
        mapName = from.getMap().name;
        waves = new ArrayList<>();

        for(Wave wave : from.getWaves()) {
            waves.add(new WaveData(wave));
        }
    }
}
