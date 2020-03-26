package io.github.zap.zombiesplugin.map;

import com.google.gson.Gson;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.map.data.RoundData;
import io.github.zap.zombiesplugin.map.data.WaveData;
import io.github.zap.zombiesplugin.map.round.Round;
import io.github.zap.zombiesplugin.map.round.Wave;
import io.github.zap.zombiesplugin.map.spawn.SpawnManager;
import io.github.zap.zombiesplugin.map.spawn.SpawnManagerImporter;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.Importer;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.nio.file.Path;
import java.util.ArrayList;

public class GameMapImporter extends Importer {
    private Gson fileParser;

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        fileParser = manager.getGsonBuilder().create();
    }

    @Override
    public void registerValue(String name, Object value) {

    }

    @Override
    public void processConfigFile(Path file, String contents) {
        GameMapData data = fileParser.fromJson(contents, GameMapData.class);
        GameMap map = new GameMap(data.mapName);

        SpawnManagerImporter importer = ZombiesPlugin.instance.getConfigManager().getImporter("SpawnPointManager");
        ArrayList<SpawnManager> managers = importer.getSpawnManagers();
        if(managers != null) {
            for(SpawnManager manager : managers) {
                map.add(manager);
            }
        }

        for(RoundData roundData : data.rounds) {
            ArrayList<Wave> wave = new ArrayList<>();
            for(WaveData waveData : roundData.waves) {
                ArrayList<MythicMob> mobs = new ArrayList<>();
                for(String name : waveData.mobNames) {
                    mobs.add(MythicMobs.inst().getAPIHelper().getMythicMob(name));
                }

                wave.add(new Wave(waveData.delay, mobs));
            }
            map.add(new Round(map, wave));
        }

        ZombiesPlugin.instance.addMap(map, map.name);
    }

    @Override
    public String getConfigExtension() {
        return "gameMap";
    }
}
