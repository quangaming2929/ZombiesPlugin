package io.github.zap.zombiesplugin.manager;

import io.lumine.xikage.mythicmobs.players.PlayerManager;

/**
 * Performs players presence check and other action might have multiple implementation.
 * Also allow client to access & hook events in game manager
 */
public abstract class GameManagerHook {
    private final GameManager manager;

    public GameManagerHook(GameManager manager) {
        this.manager = manager;
    }

    public GameManager getManager() {
        return manager;
    }

    public UserManager getUserManager () {
        return manager.getUserManager();
    }

    /**
     * Unregister events,...
     */
    protected abstract void onGameEnded();
}
