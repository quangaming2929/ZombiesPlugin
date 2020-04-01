package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.round.Round;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GameManager {
    public final String name;

    private GameSettings settings;
    private PlayerManager playerManager;
    private boolean hasEnded;

    private int lastRound = 0;

    public GameManager(String name, GameSettings settings) {
        this.name = name;
        this.settings = settings;
        playerManager = new PlayerManager(this);
    }

    public void startNextRound() {
        ArrayList<Round> rounds = settings.gameMap.getRounds();

        if (lastRound == rounds.size()) {
            // TODO: Endgame sequence
        } else {
            rounds.get(lastRound).startRound(this, settings.difficulty);
            lastRound++;
        }
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameMap getMap() {
        return settings.gameMap;
    }

    public int getGameSize() {return settings.gameSize; }

    public boolean hasPlayer(Player player) {
        return playerManager.hasUser(playerManager.getAssociatedUser(player));
    }

    public String toString() { return name; }

    public boolean hasEnded() {return hasEnded;}
}
