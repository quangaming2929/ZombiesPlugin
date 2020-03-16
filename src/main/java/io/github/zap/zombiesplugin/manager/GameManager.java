package io.github.zap.zombiesplugin.manager;

public class GameManager {
    private GameSettings settings;
    private PlayerManager playerManager;

    /**
     * A GameManager instance is created for every game.
     * @param settings The settings to start the game with.
     */
    public GameManager(GameSettings settings) {
        this.settings = settings;
        playerManager = new PlayerManager(this);
    }
}
