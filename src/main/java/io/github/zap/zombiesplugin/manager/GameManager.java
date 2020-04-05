package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.events.UserJoinLeaveEventArgs;
import io.github.zap.zombiesplugin.map.round.Round;
import java.util.List;

import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;
import io.github.zap.zombiesplugin.scoreboard.InGameScoreBoard;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class GameManager {
    public final String name;
    private GameSettings settings;
    private GameState state;
    private PlayerManager playerManager;
    private IInGameScoreboard scoreboard;

    private int currentRound = 0;


    public GameManager(String name, GameSettings settings) {
        this.name = name;
        this.settings = settings;
        this.scoreboard = new InGameScoreBoard(this);
        playerManager = new PlayerManager(this);
        playerManager.getPlayerJoinLeaveHandler().registerEvent(this::onPlayerChange);
    }

    public String getName() { return name; }

    public GameSettings getSettings() { return settings; }

    public PlayerManager getPlayerManager() { return playerManager; }

    public GameState getState() { return state; }

    /**
     * Gets the zero-based round index.
     * @return
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
        //TODO: timer code here
    }

    public void stopCountdown() {
        //TODO: abort timer
    }

    public void startGame() {
        ArrayList<Round> rounds = settings.getGameMap().getRounds();

        if (currentRound == rounds.size()) {
            // TODO: Endgame sequence
        } else {
            rounds.get(currentRound).startRound(this, settings.getDifficulty());
            currentRound++;
        }
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        if(state == GameState.STARTED) {
            AbstractEntity mob = event.getMob().getEntity();
            if(mob.hasMetadata("zp_manager") && mob.getMetadata("zp_manager").isPresent()) {
                GameManager manager = (GameManager)mob.getMetadata("zp_manager").get();
                if(manager == this) {
                    currentMobCount--;
                    if(currentMobCount == 0) {
                        currentRoundIndex++;
                        startRound(currentRoundIndex);
                    }
                }
            }
        }
    }

    private void startRound(int index) {
        if(state == GameState.COUNTDOWN) {
            state = GameState.STARTED;
        }

        ArrayList<Round> rounds = settings.getGameMap().getRounds();

        if (index == rounds.size()) {
            state = GameState.WON;
            // TODO: Endgame sequence
        } else {
            currentRound = rounds.get(index);

            ArrayList<Wave> waves = currentRound.getWaves();
            for(Wave wave : waves) {
                currentMobCount += wave.getMobs(settings.getDifficulty()).size();
            }

            currentRound.start(this);
        }
    }
}
