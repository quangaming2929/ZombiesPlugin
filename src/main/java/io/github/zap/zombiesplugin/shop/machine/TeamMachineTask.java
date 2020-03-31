package io.github.zap.zombiesplugin.shop.machine;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.data.ICost;
import io.github.zap.zombiesplugin.shop.machine.data.TMTaskData;

public abstract class TeamMachineTask {
    protected GameManager manager;
    protected int boughtCount = 0;
    protected ICost cost;

    public TeamMachineTask(TMTaskData data, GameManager manager) {
        this.cost = data.cost;
        this.manager = manager;
    }

    public void tryPurchase(User executor) {
        int cost = getCost();
        if(executor.getGold() >= cost) {
            if (execTask(executor)) {
                executor.addGold(-cost);
            }
        }
    }

    public abstract boolean execTask(User user);

    public int getCost() {
        return cost.getCost(manager, boughtCount);
    }
}
