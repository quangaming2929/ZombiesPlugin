package io.github.zap.zombiesplugin.provider;

import com.google.gson.GsonBuilder;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.GunData;
import io.github.zap.zombiesplugin.data.cost.StaticCost;
import io.github.zap.zombiesplugin.data.leveling.ListLeveling;
import io.github.zap.zombiesplugin.data.soundfx.SingleNoteSoundFx;
import io.github.zap.zombiesplugin.data.ultvalue.EquipmentValue;
import io.github.zap.zombiesplugin.data.ultvalue.LoreEquipmentValue;
import io.github.zap.zombiesplugin.data.visuals.DefaultWeaponVisual;
import io.github.zap.zombiesplugin.data.visuals.ExpressionVisual;
import io.github.zap.zombiesplugin.map.data.GameMapData;
import io.github.zap.zombiesplugin.utils.IOHelper;
import io.gsonfire.GsonFireBuilder;
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
        //registerCustomClasses();

        if (!configDir.exists()) {
            configDir.mkdir();
        }
    }

    private void registerCustomClasses() {
        // Equipment custom class section
        // Data files
        registerCustomClass("EquipmentData", EquipmentData.class);
        registerCustomClass("GunData", GunData.class);

        // IDefaultVisual
        registerCustomClass("DefaultWeaponVisual", DefaultWeaponVisual.class);
        registerCustomClass("ExpressionVisual", ExpressionVisual.class);

        // Levels
        registerCustomClass("ListLeveling", ListLeveling.class);
        registerCustomClass("EquipmentValue", EquipmentValue.class);
        registerCustomClass("LoreEquipmentValue", LoreEquipmentValue.class);

        // Melee skill <Their is no subclass of MeleeSkill at the moment

        // SoundFx
        registerCustomClass("SingleNoteSoundFx", SingleNoteSoundFx.class);

        // Cost
        registerCustomClass("StaticCost", StaticCost.class);
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
     * @return The gson builder
     */
    public GsonBuilder getGsonBuilder() {
        return new GsonFireBuilder()
                .registerTypeSelector(ICustomSerializerIdentity.class, new CustomClassSelector(this))
                .registerPostProcessor(ICustomSerializerIdentity.class, new CustomClassPostProcessor(this))
                .createGsonBuilder()
                .serializeNulls();
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
        try (Stream<Path> paths = Files.walk(Paths.get(this.rootDir.getAbsolutePath()))) {
            Importer importer = getImporter(name);
            String ext = importer.getConfigExtension();
            String finalExt = ext.startsWith(".") ? ext : "." + ext;

            paths.filter(path -> path.toString().endsWith(finalExt))
                    .forEach(path -> importer.processConfigFile(path, IOHelper.readFile(path)));

        }
        catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to reload importer" + name);
        }
    }
}
