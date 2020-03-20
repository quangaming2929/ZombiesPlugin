package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.round.Round;
import java.util.List;
import org.bukkit.entity.Player;

public class GameManager {
    //map is stored in GameSettings
    private GameSettings settings;
    private PlayerManager playerManager;

    private List<Player> players;

    /**
     * The last round the game was on
     */
    private int lastRound = 0;

    /**
     * A GameManager instance is created for every game.
     * @param settings The settings to start the game with.
     */
    public GameManager(GameSettings settings) {
        this.settings = settings;
        System.out.println("GameManager instance created.");
        playerManager = new PlayerManager(this);
    }

    /**
     * Starts the next round
     */
    public void startNextRound() {
        Round[] rounds = settings.map.getRounds();

        if (lastRound == rounds.length) {
            // TODO: Endgame sequence
        } else {
            rounds[lastRound].startRound();
            lastRound++;
        }
    }

    /** Gets the player manager
     *
     * @return The player manager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /** Gets the game map
     *
     * @return The map
     */
    public Map getMap() {
        return settings.map;
    }
}
