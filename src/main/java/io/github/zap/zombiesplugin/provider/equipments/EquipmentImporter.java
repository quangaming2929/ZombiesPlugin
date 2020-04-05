package io.github.zap.zombiesplugin.provider.equipments;

import com.google.gson.Gson;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.equipments.Equipment;
import io.github.zap.zombiesplugin.manager.PlayerManager;
import io.github.zap.zombiesplugin.provider.ConfigFileManager;
import io.github.zap.zombiesplugin.provider.Importer;

import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public abstract class EquipmentImporter extends Importer {
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
    }

    @Override
    public void processConfigFile (Path file, String contents) {
        EquipmentData data = fileParser.fromJson(contents, getConfigType());
        finalize(data);
        if(!dataVault.containsKey(data.id)) {
            dataVault.put(data.id, data);
        } else {
            String errorMessage = "Duplicate equipment id or the equipment is already imported. Name: " + data.name + " at " + file.toString() + "replacing old import...";
            log(Level.WARNING, errorMessage);
            dataVault.replace(data.id, data);
        }
    }

    public Equipment createEquipment (String id, PlayerManager playerManager) throws Exception {
        if (dataVault.containsKey(id)) {
            EquipmentData currentData = dataVault.get(id);
            if(values.containsKey(currentData.behaviour)) {
                Class<? extends Equipment> bClazz = values.get(currentData.behaviour);
                Equipment equipment = bClazz
                        .getConstructor(EquipmentData.class, PlayerManager.class)
                        .newInstance(currentData, playerManager);
                return equipment;
            } else {
                log(Level.WARNING, "Can't find equipment behaviour for this data: " + id);
            }
        } else {
            log(Level.WARNING, "Can't find the equipment id: " + id);
        }

        return null;
    }


    public Set<Map.Entry<String, EquipmentData>> getEquipmentDataSet() {
        return dataVault.entrySet();
    }

    protected Class<? extends EquipmentData> getConfigType() {
        return EquipmentData.class;
    }

    protected abstract void finalize(EquipmentData data);
}
