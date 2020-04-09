package io.github.zap.zombiesplugin.gamecreator;

import org.bukkit.entity.Player;

public interface ILobbyProvider {
    boolean warpToLobby(Player p, String name);
}
