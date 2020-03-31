package io.github.zap.zombiesplugin.shop.machine.tasks;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachineTask;
import io.github.zap.zombiesplugin.data.cost.StaticCost;
import io.github.zap.zombiesplugin.data.TMTaskData;
import org.bukkit.Material;

import java.util.Arrays;

public class DebugTask extends TeamMachineTask {


    public DebugTask(TMTaskData data, GameManager manager) {
        super(data, manager);
    }

    @Override
    public boolean execTask(User user) {
        System.out.println("BRoooooo");
        return true;
    }

    public static DebugTask getTest() {
        TMTaskData data = new TMTaskData();
        data.cost = new StaticCost(1000);
        data.description = Arrays.asList("This is test", "bro");
        data.displayItem = Material.DRAGON_EGG;
        data.name = "Test Task";

        return new DebugTask(data, null);
    }
}
