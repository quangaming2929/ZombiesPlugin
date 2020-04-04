package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.round.Wave;

import java.util.ArrayList;

public class RoundData implements IData<Round> {
    public ArrayList<WaveData> waves;

    public RoundData() {}

    public RoundData(Round from) {
        waves = new ArrayList<>();

        for(Wave wave : from.getWaves()) {
            waves.add(new WaveData(wave));
        }
    }

    @Override
    public Round load() {
        ArrayList<Wave> result = new ArrayList<>();

        for(WaveData data : waves) {
            result.add(data.load());
        }

        return new Round(result);
    }
}
