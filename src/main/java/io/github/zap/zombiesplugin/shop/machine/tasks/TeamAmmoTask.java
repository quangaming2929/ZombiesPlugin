package io.github.zap.zombiesplugin.shop.machine.tasks;

import io.github.zap.zombiesplugin.data.TMTaskData;
import io.github.zap.zombiesplugin.equipments.guns.Gun;
import io.github.zap.zombiesplugin.hotbar.HotbarObject;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachineTask;

public class TeamAmmoTask extends TeamMachineTask {
    public TeamAmmoTask(TMTaskData data, GameManager manager) {
        super(data, manager);
    }

    @Override
    public boolean execTask(User user) {
        for(User player : manager.getUserManager().getPlayers()) {
            for (HotbarObject obj : player.getGunGroup().getObjects()) {
                if (obj instanceof Gun) {
                    ((Gun) obj).refill();
                }
            }
        }

        return true;
    }
}
