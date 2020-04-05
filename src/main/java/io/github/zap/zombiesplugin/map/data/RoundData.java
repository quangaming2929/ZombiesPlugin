package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.round.Wave;

import java.util.ArrayList;

public class RoundData implements IMapData<Round> {
    public ArrayList<WaveData> waves;

    public RoundData() {}

    public RoundData(Round from) {
        waves = new ArrayList<>();

        for(Wave wave : from.getWaves()) {
            waves.add(new WaveData(wave));
        }
    }

    @Override
    public Round load(Object args) {
        ArrayList<Wave> result = new ArrayList<>();

        for(WaveData data : waves) {
            result.add(data.load(null));
        }

        return new Round(result);
    }
}
