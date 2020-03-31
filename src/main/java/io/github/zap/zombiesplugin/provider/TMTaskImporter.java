package io.github.zap.zombiesplugin.provider;

import java.nio.file.Path;

public class TMTaskImporter extends Importer {

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerTestTask();
    }

    private void registerTestTask() {

    }

    @Override
    public void registerValue(String name, Object value) {

    }

    @Override
    public void processConfigFile(Path file, String contents) {

    }

    @Override
    public String getConfigExtension() {
        return ".disorganisation";
    }
}
