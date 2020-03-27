package io.github.zap.zombiesplugin.map;

import java.util.Hashtable;

public interface IConverterRegister {
    Hashtable<String, IReflectionConverter> converters = new Hashtable<>();

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
     boolean registerValue(String name, IReflectionConverter converter);
}
