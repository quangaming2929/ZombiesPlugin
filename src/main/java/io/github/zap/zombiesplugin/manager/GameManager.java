package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.map.round.Round;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class GameManager {
    public final String id;

    private GameSettings settings;
    private PlayerManager playerManager;

    /**
     * The map of the game
     */
    private Map map;

    /**
     * The last round the game was on
     */
    private int lastRound = 0;

    /**
     * A GameManager instance is created for every game.
     * @param settings The settings to start the game with.
     */
    public GameManager(GameSettings settings, Map map, String id) {
        this.settings = settings;
        this.map = map;
        this.id = id;
        playerManager = new PlayerManager(this);
    }

    public GameManager(String id) {
        this.id = id;
    }

    public void build(GameSettings settings, Map map) {
        this.settings = settings;
        this.map = map;
        playerManager = new PlayerManager(this);
    }

    /**
     * Starts the next round
     */
    public void startNextRound() {
        ArrayList<Round> rounds = map.getRounds();

        if (lastRound == rounds.size()) {
            // TODO: Endgame sequence
        } else {
            rounds.get(lastRound).startRound();
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
        return map;
    }

    public int getGameSize() {return settings.gameSize; }

    public boolean hasPlayer(Player player) {
        return playerManager.hasUser(playerManager.getAssociatedUser(player));
    }
}
