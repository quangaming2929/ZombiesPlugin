package io.github.zap.zombiesplugin.provider;

import com.google.gson.GsonBuilder;
import io.github.zap.zombiesplugin.guns.data.leveling.UltimateLevelingList;
import io.github.zap.zombiesplugin.utils.IOHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

public class ConfigFileManager {
    private Hashtable<String, Importer> importers = new Hashtable<>();

    private final JavaPlugin plugin;
    private final Hashtable<String, Class<? extends ICustomSerializerIdentity>> customClasses;
    private final File rootDir;

    public ConfigFileManager(JavaPlugin plugin, File configDir) {
        this.plugin = plugin;
        this.rootDir = configDir;
        this.customClasses = new Hashtable<>();
        registerCustomClasses();

        if (!configDir.exists()) {
            configDir.mkdir();
        }
    }

    private void registerCustomClasses() {
        registerCustomClass("LevelList", UltimateLevelingList.class);
    }

    /**
     * Register custom class for serializing. Note: Any dependency of this plugin use
     * this class if you want to resolve an interface, abstract class
     */
    public void registerCustomClass(String friendlyName, Class<? extends  ICustomSerializerIdentity> classToRegister) {
        customClasses.put(friendlyName, classToRegister);
    }

    /**
     * This method is used by Gson Type Adapter to resolve custom types
     * @param name The familiar type name
     */
    public Class<? extends  ICustomSerializerIdentity> getCustomClass(String name) {
        if(customClasses.containsKey(name)) {
            return customClasses.get(name);
        }

        return null;
    }

    /**
     * This method is used by Gson Type Adapter to resolve custom types
     * @param cc The custom class
     */
    public String getCustomClassFriendlyName (Class<? extends ICustomSerializerIdentity> cc) {
        for (Map.Entry<String, Class<? extends ICustomSerializerIdentity>> item : customClasses.entrySet()) {
            if (cc == item.getValue()) {
                return item.getKey();
            }
        }

        return null;
    }

    /**
     * Get the GsonBuilder for this provider, it contains the type adapter for resolve
     * registered custom type of this provider
     * @return
     */
    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeHierarchyAdapter( ICustomSerializerIdentity.class, new CustomClassGsonAdapter(this));
    }

    public void registerValue(String importerName, String valueName, Object value) {
        if (importers.containsKey(importerName)) {
            importers.get(importerName).registerValue(valueName, value);
        }
    }

    public boolean addImporter (String name, Importer importer) {
        if(!importers.containsKey(name)) {
            importer.init(this);
            importers.put(name, importer);
            return true;
        }

        return false;
    }

    public <T extends Importer> T getImporter(String name) {
        return importers.containsKey(name) ? (T)importers.get(name) : null;
    }

    public void reload() {
        for (String name : importers.keySet()) {
            reload(name);
        }
    }

    public void reload(String name){
        try (Stream<Path> paths = Files.walk(Paths.get(this.rootDir.getPath()))) {
            Importer importer = getImporter(name);
            String ext = importer.getConfigExtension();

            paths.filter(path -> path.endsWith(ext))
                    .forEach(path -> importer.processConfigFile(path, IOHelper.readFile(path)));
        }
        catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to reload importer" + name);
        }
    }
}
