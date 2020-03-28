package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.GameMap;
import io.github.zap.zombiesplugin.map.round.Round;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GameManager {
    public final String name;

    private GameSettings settings;
    private PlayerManager playerManager;
    private GameMap map;
    private int lastRound = 0;

    public GameManager(String name, GameSettings settings, GameMap map) {
        this.name = name;

        this.settings = settings;
        playerManager = new PlayerManager(this);
        this.map = map;
    }

    public void startNextRound() {
        ArrayList<Round> rounds = map.getRounds();

        if (lastRound == rounds.size()) {
            // TODO: Endgame sequence
        } else {
            rounds.get(lastRound).startRound();
            lastRound++;
        }
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameMap getMap() {
        return map;
    }

    public int getGameSize() {return settings.gameSize; }

    public boolean hasPlayer(Player player) {
        return playerManager.hasUser(playerManager.getAssociatedUser(player));
    }
}
