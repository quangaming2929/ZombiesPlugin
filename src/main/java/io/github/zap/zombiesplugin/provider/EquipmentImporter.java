package io.github.zap.zombiesplugin.provider;

import com.google.gson.Gson;
import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.data.GunData;
import io.github.zap.zombiesplugin.equipments.Equipment;
import io.github.zap.zombiesplugin.guns.Gun;

import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public abstract class EquipmentImporter extends Importer {
    protected Gson fileParser;

    protected Hashtable<String, EquipmentData> dataVault = new Hashtable<>();
    public Hashtable<String, Class> values = new Hashtable<>();


    @Override
    public boolean registerValue(String name, Object value) {
        if (value instanceof Class) {
            if (values.containsKey(name)) {
                values.replace(name, (Class) value);
            } else {
                values.put(name, (Class) value);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        fileParser = manager.getGsonBuilder().create();
    }

    @Override
    public void processConfigFile(Path file, String contents) {
        EquipmentData data = fileParser.fromJson(contents, getConfigType());
        finalize(data);
        if(!dataVault.containsKey(data.id)) {
            dataVault.put(data.id, data);
        } else {
            String errorMessage = "Error: duplicate equipment id or the equipment is already imported. Name: " + data.name + " at " + file.toString();
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, errorMessage);
        }
    }

    public Equipment createEquipment (String id) throws Exception {
        if (dataVault.containsKey(id)) {
            EquipmentData currentData = dataVault.get(id);
            if(values.containsKey(currentData.behaviour)) {
                Class<? extends Equipment> bClazz = values.get(currentData.behaviour);
                Equipment equipment = bClazz.getConstructor(EquipmentData.class).newInstance(currentData);
                return equipment;
            } else {
                ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find equipment behaviour for this data: " + id);
            }
        } else {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Can't find the equipment id: " + id);
        }

        return null;
    }


    public Set<Map.Entry<String, EquipmentData>> getGunDatas() {
        return dataVault.entrySet();
    }

    protected Class<? extends EquipmentData> getConfigType() {
        return EquipmentData.class;
    }

    protected abstract void finalize(EquipmentData data);
}
