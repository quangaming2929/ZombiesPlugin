package io.github.zap.zombiesplugin.gamecreator;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.map.GameMap;
import org.bukkit.entity.Player;

/**
 * Provides the arena for the current room
 */
public interface IArenaProvider {
    GameManager createGame(GameRoom room);
    boolean wrapPlayer(GameRoom room, Player player);
}
