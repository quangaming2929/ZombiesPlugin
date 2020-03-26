package io.github.zap.zombiesplugin.provider;


import io.github.zap.zombiesplugin.map.data.IReflectionConverter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;

public abstract class Importer {
    protected ConfigFileManager manager;
    protected Hashtable<String,IReflectionConverter> converters;

    /**
     * Register a custom converter. This can be used to implement custom spawning logic beyond what the default
     * SpawnManagers do.
     *
     * The converter is given an object which is always a subclass of SpawnManagerData and a Class, which is always
     * the Class object of a subclass of SpawnpointManager. The function should return a subclass of SpawnpointManager.
     *
     * The reason this exists is to convert between a GSON-serializable "data holder" object and the actual
     * SpawnpointManager that is directly used by the plugin. The SpawnpointManager itself cannot be serialized.
     *
     * The validity of the SpawnPointData is checked before being passed to the converter. The converter will never
     * be called with SpawnPointData that contains an invalid map.
     *
     * @param name The unique identifier used by this converter
     * @param converter The converter function
     * @return True if the converter was registered successfully, false if one with the same name already exists.
     */
    public boolean registerValue(String name, IReflectionConverter converter) {
        if(!converters.containsKey(name)) {
            converters.put(name, converter);
            return true;
        }
        else return false;
    }

    /**
     * This methods will be called when added to a manager. Override this method to init
     * the resources you need for this importer
     * @param manager the host manager
     */
    public void init(ConfigFileManager manager) {
        this.manager = manager;
        this.converters = new Hashtable<>();
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
