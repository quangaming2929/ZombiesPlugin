package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.Importer;

import java.nio.file.Path;

public class SpawnManagerImporter extends Importer {
    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
    }

    @Override
    public void processConfigFile(Path file, String contents) {

    }

    @Override
    public String getConfigExtension() {
        return "spawnManagers";
    }
}
