package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.events.EventArgs;
import io.github.zap.zombiesplugin.events.EventHandler;
import io.github.zap.zombiesplugin.events.UserJoinLeaveEventArgs;
import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.map.round.Round;

import io.github.zap.zombiesplugin.player.PlayerState;
import io.github.zap.zombiesplugin.player.User;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;
import io.github.zap.zombiesplugin.scoreboard.InGameScoreBoard;

import java.util.ArrayList;

import io.github.zap.zombiesplugin.map.round.Wave;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class GameManager implements Listener {
    public final String name;


    private GameSettings settings;
    private UserManager userManager;
    private GameState state;
    private GameSession session;

    private int currentMobCount = 0;
    private int currentRoundIndex = 0;
    private Round currentRound;

    public GameManager(String name, GameSettings settings) {
        this.name = name;
        this.settings = settings;

        userManager = new UserManager(this);
        userManager.getPlayerJoinLeaveHandler().registerEvent(this::onPlayerChange);

        ZombiesPlugin.instance.getServer().getPluginManager().registerEvents(this, ZombiesPlugin.instance);
    }

    public String getName() { return name; }

    public GameSettings getSettings() { return settings; }

    public UserManager getUserManager() { return userManager; }

    public GameState getState() { return state; }

    /**
     * Gets the zero-based round index.
     * @return The zero-based round index
     */
    public int getCurrentRound() { return currentRoundIndex; }

    public boolean runAI() { return state == GameState.STARTED; }

    private void onPlayerChange(Object sender, @NotNull UserJoinLeaveEventArgs e) {
        switch(e.type) {
            case JOIN:
                if(state == GameState.PREGAME && userManager.getPlayers().size() == settings.getGameSize()) {
                    state = GameState.COUNTDOWN;
                }
                break;
            case LEAVE:
                if(state == GameState.COUNTDOWN) {
                    state = GameState.PREGAME;
                }
                else if(state == GameState.STARTED) {
                    if(userManager.getPlayers().size() == 0) {
                        state = GameState.CANCELED;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + e.type.toString());
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

    @org.bukkit.event.EventHandler
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

    public void setGameSession(GameSession session) {
        this.session = session;
    }

    public IInGameScoreboard getScoreboard() {
        return session.scoreboard;
    }

    public GameRoom getContainingRoom () {
        return session.room;
    }

    public GameManagerHook getGameHook () {
        return session.hook;
    }

    public void setState(GameState state) {
        this.state = state;

        if (state == GameState.STARTED) {
            for(User user : getUserManager().getPlayers()) {
                user.setState(PlayerState.ALIVE);
            }
        }

        if (session.scoreboard != null) {
            session.scoreboard.setGameState(state);
        }
    }

    public final EventHandler<EventArgs> gameEnded = new EventHandler<>();
    protected void onGameEnded() {
        if(session.hook != null)
            session.hook.onGameEnded();

        gameEnded.invoke(this, EventArgs.Empty);
    }
}
