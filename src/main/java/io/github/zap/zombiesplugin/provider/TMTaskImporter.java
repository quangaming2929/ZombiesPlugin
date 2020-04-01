package io.github.zap.zombiesplugin.provider;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.data.TMTaskData;
import io.github.zap.zombiesplugin.data.cost.StaticCost;
import io.github.zap.zombiesplugin.data.soundfx.SingleNoteSoundFx;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.shop.machine.TeamMachineTask;
import io.github.zap.zombiesplugin.shop.machine.tasks.DragonWrathTask;
import io.github.zap.zombiesplugin.shop.machine.tasks.TeamAmmoTask;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;

public class TMTaskImporter extends Importer {
    public Hashtable<String, Class<? extends TeamMachineTask>> tasks = new Hashtable<>();
    public Hashtable<String, TMTaskData> taskDataSet = new Hashtable<>();

    @Override
    public void init(ConfigFileManager manager) {
        super.init(manager);
        registerValues();
    }

    private void registerValues() {
        registerValue("TeamAmmo", TeamAmmoTask.class);
        registerValue("DragonWrath", DragonWrathTask.class);
        tes();
    }

    private void tes() {
        TMTaskData data = new TMTaskData();
        data.id = "tmt_ammo_supply";
        data.name = "Ammo Supply";
        data.displayItem = Material.ARROW;
        data.taskName = "TeamAmmo";
        data.description = Arrays.asList("Gains max ammo for all of yours", "and your teammates' weapon");
        data.cost = new StaticCost(1000);

        SingleNoteSoundFx fx = new SingleNoteSoundFx();
        fx.sound = Sound.ENTITY_PLAYER_LEVELUP;
        fx.volume = 100;
        fx.pitch = 1;
        data.purchaseFx = fx;

        taskDataSet.put(data.id, data);

        TMTaskData dw = new TMTaskData();
        dw.id = "tmt_dragon_wrath";
        dw.name = "Dragon's Wrath";
        dw.displayItem = Material.DRAGON_EGG;
        dw.taskName = "DragonWrath";
        dw.description = Arrays.asList("Kill all enemies within a <ZZ>", "block radius after a short", "delay");
        dw.cost = new StaticCost(3000);

        SingleNoteSoundFx dwfxx = new SingleNoteSoundFx();
        dwfxx.sound = Sound.ENTITY_ENDER_DRAGON_GROWL;
        dwfxx.volume = 100;
        dwfxx.pitch = 1;
        dw.purchaseFx = dwfxx;
        dw.customData = new Hashtable<>();
        dw.customData.put(DragonWrathTask.TMT_WRATH_RADIUS, "15");
        dw.customData.put(DragonWrathTask.TMT_WRATH_DELAY, "2");

        taskDataSet.put(dw.id, dw);
    }

    public TeamMachineTask createTask(String id, GameManager manager) throws Exception{
        if (taskDataSet.containsKey(id)) {
            TMTaskData taskData = taskDataSet.get(id);
            if (tasks.containsKey(taskData.taskName)) {
                Class<? extends TeamMachineTask> taskClazz = tasks.get(taskData.taskName);
                return taskClazz
                        .getConstructor(TMTaskData.class, GameManager.class)
                        .newInstance(taskData, manager);
            } else {
                log(Level.WARNING, "Can't find the requested task implementation. Task name: " + taskData.taskName);
            }
        } else {
            log(Level.WARNING, "Can't find the requested task id: " + id);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")  // Parameterized Class<T> value is not checked.
    public boolean registerValue(String name, Object value) {
        try {
            if (value instanceof Class) {
                if (tasks.containsKey(name)) {
                    tasks.replace(name, (Class<? extends TeamMachineTask>) value);
                } else {
                    tasks.put(name, (Class<? extends TeamMachineTask>) value);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log(Level.WARNING, "invalid register value in task importer. Task name: " + name);
            return false;
        }
    }

    @Override
    public void processConfigFile(Path file, String contents) {
        TMTaskData data = fileParser.fromJson(contents, TMTaskData.class);
        if (taskDataSet.containsKey(data.id)) {
            String errorMessage = "Duplicate task id or the task is already imported. Name: " + data.name + " at " + file.toString() + "replacing old import...";
            log(Level.WARNING, errorMessage);
            taskDataSet.replace(data.id, data);
        } else {
            taskDataSet.put(data.id, data);
        }
    }

    @Override
    public String getConfigExtension() {
        return "tmtData";
    }
}
