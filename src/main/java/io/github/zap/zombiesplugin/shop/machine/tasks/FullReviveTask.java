package io.github.zap.zombiesplugin.shop.machine.tasks;

import io.github.zap.zombiesplugin.data.TMTaskData;
import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.shop.machine.TeamMachineTask;

// TODO: Untested Task
public class FullReviveTask extends TeamMachineTask {
    public FullReviveTask(TMTaskData data, GameManager manager) {
        super(data, manager);
    }

    @Override
    public boolean execTask(User user) {
        for (User player : manager.getUserManager().getPlayers()) {
            if (player.getState() == PlayerState.DEAD || player.getState() == PlayerState.KNOCKED_DOWN) {
                player.setState(PlayerState.ALIVE);
            }
        }

        return true;
    }
}
