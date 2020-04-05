package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import io.github.zap.zombiesplugin.events.UserJoinLeaveEventArgs;
import io.github.zap.zombiesplugin.map.round.Round;

import java.util.ArrayList;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class GameManager implements Listener {
    private final String name;

    private GameSettings settings;
    private UserManager userManager;
    private GameState state;

    private int currentMobCount = 0;
    private int currentRound = 0;


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
    public int getCurrentRound() { return currentRound; }

    public boolean hasEnded() { return state == GameState.CANCELED || state == GameState.LOST || state == GameState.WON; }

    private void onPlayerChange(Object sender, @NotNull UserJoinLeaveEventArgs e) {
        switch(e.type) {
            case JOIN:
                if(state == GameState.PREGAME && userManager.getPlayers().size() == settings.getGameSize()) {
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
            rounds.get(currentRound).start(this);
            currentRound++;
        }
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        AbstractEntity mob = event.getMob().getEntity();
        if(mob.hasMetadata("zp_manager") && mob.getMetadata("zp_manager").isPresent()) {
            GameManager manager = (GameManager)mob.getMetadata("zp_manager").get();
            if(manager == this) {

            }
        }
    }
}
