package io.github.zap.zombiesplugin.provider;


import java.io.File;
import java.nio.file.Path;

public abstract class Importer {
    protected ConfigFileManager manager;

    public abstract boolean registerValue(String name, Object value);

    /**
     * This methods will be called when added to a manager. Override this method to init
     * the resources you need for this importer
     * @param manager the host manager
     */
    public void init(ConfigFileManager manager) {
        this.manager = manager;
    }

    /**
     * Process the config file
     * @param file the path to this config file
     * @param contents the content of the file
     */
    public abstract void processConfigFile (Path file, String contents);

    /**
     * get the config file extension
     */
    public abstract String getConfigExtension();

    /**
     * Called before the manager reload this Importer
     */
    public void onReload() {}
}
