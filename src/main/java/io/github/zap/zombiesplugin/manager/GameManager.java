package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.Map;
import io.github.zap.zombiesplugin.map.round.Round;
import java.util.List;

import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;
import io.github.zap.zombiesplugin.scoreboard.InGameScoreBoard;
import org.bukkit.entity.Player;

public class GameManager {
    private GameSettings settings;
    private GameState state;
    private PlayerManager playerManager;
    private IInGameScoreboard scoreboard;

    /**
     * The map of the game
     */
    private final Map map;
    // No final here because we might allow client to have their own scoreboard
    private InGameScoreBoard sb;

    private List<Player> players;

    /**
     * The last round the game was on
     */
    private int lastRound = 0;

    /**
     * A GameManager instance is created for every game.
     * @param settings The settings to start the game with.
     */
    public GameManager(GameSettings settings, Map map) {
        this.settings = settings;
        this.map = map;
        this.sb = new InGameScoreBoard(this);
        playerManager = new PlayerManager(this);
    }

    /**
     * Starts the next round
     */
    public void startNextRound() {
        Round[] rounds = map.getRounds();

        if (lastRound == rounds.length) {
            // TODO: Endgame sequence
        } else {
            rounds[lastRound].startRound();
            if (scoreboard != null) {
                scoreboard.setRound(lastRound);
            }

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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;

        if (state == GameState.STARTED) {
            for(User user :getPlayerManager().getPlayers()) {
                user.setState(PlayerState.ALIVE);
            }
        }

        if (scoreboard != null) {
            scoreboard.setGameState(state);
        }
    }

    public IInGameScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(IInGameScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
}
