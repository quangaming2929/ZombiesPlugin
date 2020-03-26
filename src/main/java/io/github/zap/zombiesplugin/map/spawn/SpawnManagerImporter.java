package io.github.zap.zombiesplugin.map.spawn;

import com.google.gson.Gson;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.data.IReflectionConverter;
import io.github.zap.zombiesplugin.map.data.SpawnManagerData;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.Importer;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SpawnManagerImporter extends Importer {
    private Gson fileParser;
    private SpawnManagerContainer container;
    private HashMap<String,SpawnManager> managers;

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        fileParser = manager.getGsonBuilder().create();
        container = new SpawnManagerContainer();
        managers = new HashMap<>();

        converters.put("ordered", (Object input, Class target) -> {
            SpawnManagerData data = (SpawnManagerData)input;
            HashSet<MythicMob> mobs = new HashSet<>();
            for(String mobName : data.mobNames) {
                mobs.add(MythicMobs.inst().getAPIHelper().getMythicMob(mobName));
            }

            return new OrderedSpawnManager(data.name, ZombiesPlugin.instance.getMap(data.mapName), mobs);
        });

        converters.put("randomized", (Object input, Class target) -> {
            SpawnManagerData data = (SpawnManagerData)input;
            HashSet<MythicMob> mobs = new HashSet<>();
            for(String mobName : data.mobNames) {
                mobs.add(MythicMobs.inst().getAPIHelper().getMythicMob(mobName));
            }

            return new RandomizedSpawnManager(data.name, ZombiesPlugin.instance.getMap(data.mapName), mobs);
        });
    }

    @Override
    public void processConfigFile(Path file, String contents) {
        container = fileParser.fromJson(contents, SpawnManagerContainer.class);

        for(SpawnManagerData data : container.managers) {
            GameMap map = ZombiesPlugin.instance.getMap(data.mapName);
            if(map != null) {
                IReflectionConverter converter = converters.get(data.typeConverter);
                if(converter != null) {
                    Object result = converter.convert(data, SpawnManager.class);

                    if(result instanceof SpawnManager) {
                        SpawnManager manager = (SpawnManager)result;
                        managers.put(manager.name, manager);
                    }
                    else {
                        //TODO: handle reflection mishap

                        /*
                        in the case of a custom API, the implementing code may not have extended from SpawnManagerData
                        and SpawnManager.
                         */
                    }
                }
                else {
                    //TODO: handle invalid (nonexistant) converter
                }
            }
            else {
                //TODO: handle invalid (nonexistant) map
            }
        }
    }

    @Override
    public String getConfigExtension() {
        return "spawnManagers";
    }

    public ArrayList<SpawnManager> getSpawnManagers() {
        if(managers.size() > 0) return new ArrayList<>(managers.values());
        return null;
    }

    public SpawnManager getByName(String name) {
        return managers.get(name);
    }
}
