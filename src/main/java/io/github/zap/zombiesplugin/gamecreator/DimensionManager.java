package io.github.zap.zombiesplugin.gamecreator;

import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DimensionManager {
    private final Random rand = new Random();
    private final List<String> lobbyNames = new ArrayList<>();
    private final IArenaProvider arenaProvider;
    private final ILobbyProvider lobbyProvider;

    public DimensionManager(IArenaProvider arenaProvider, ILobbyProvider lobbyProvider) {
        this.arenaProvider = arenaProvider;
        this.lobbyProvider = lobbyProvider;
    }

    public GameManager createGame(GameRoom room) {
        return arenaProvider.createGame(room);
    }

    public boolean warpPlayerToGame (GameRoom room, Player p) {
        return arenaProvider.wrapPlayer(room, p);
    }

    public boolean warpToRandomLobby (Player p) {
        String lobbyName = getLobbyNames().get(rand.nextInt(lobbyNames.size()));
        return warpToLobby(p, lobbyName);
    }

    public boolean warpToLobby (Player p, String lobby) {
        return lobbyProvider.warpToLobby(p, lobby);
    }

    public IArenaProvider getArenaProvider() {
        return arenaProvider;
    }

    public ILobbyProvider getLobbyProvider() {
        return lobbyProvider;
    }

    public List<String> getLobbyNames() {
        return lobbyNames;
    }
}
