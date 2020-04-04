package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.events.UserJoinLeaveEventArgs;
import io.github.zap.zombiesplugin.map.round.Round;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class GameManager {
    public final String name;

    private GameSettings settings;
    private PlayerManager playerManager;
    private GameState state;

    private int currentRound = 0;


    public GameManager(String name, GameSettings settings) {
        this.name = name;
        this.settings = settings;

        playerManager = new PlayerManager(this);
        playerManager.getPlayerJoinLeaveHandler().registerEvent(this::onPlayerChange);
    }

    public String getName() { return name; }

    public GameSettings getSettings() { return settings; }

    public PlayerManager getPlayerManager() { return playerManager; }

    public GameState getState() { return state; }

    /**
     * Gets the zero-based round index.
     * @return The zero-based round index
     */
    public int getCurrentRound() { return currentRound; }

    public boolean hasEnded() { return state == GameState.CANCELED || state == GameState.LOST || state == GameState.WON; }

    private void onPlayerChange(Object sender, @NotNull UserJoinLeaveEventArgs e) {
        switch(e.type) {
            case JOIN:
                if(state == GameState.PREGAME && playerManager.getPlayers().size() == settings.getGameSize()) {
                    state = GameState.COUNTDOWN;
                    startCountdown();
                }
                break;
            case LEAVE:
                if(state == GameState.COUNTDOWN) {
                    state = GameState.PREGAME;
                    stopCountdown();
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + e.type.toString());
        }
    }

    public void startCountdown() {
        //TODO: timer code
    }

    public void stopCountdown() {
        //TODO: abort timer
    }

    public void startGame() {
        ArrayList<Round> rounds = settings.getGameMap().getRounds();

        if (currentRound == rounds.size()) {
            // TODO: Endgame sequence
        } else {
            rounds.get(currentRound).start(this, settings.getDifficulty());
            currentRound++;
        }
    }
}
