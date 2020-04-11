package io.github.zap.zombiesplugin.map.serialize;

import com.google.gson.stream.JsonReader;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.map.data.IMapData;
import io.github.zap.zombiesplugin.provider.Importer;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;

public class GameMapImporter extends Importer {
    @Override
    public boolean registerValue(String name, Object value) {
        return true;
    }

    @Override
    public void processConfigFile(Path file, String contents) {
        IMapData mapData = fileParser.fromJson(contents, IMapData.class);
        GameMap map = (GameMap)mapData.load(null);
        ZombiesPlugin.instance.addMap(map);
    }

    @Override
    public String getConfigExtension() {
        return "mapData";
    }
}
