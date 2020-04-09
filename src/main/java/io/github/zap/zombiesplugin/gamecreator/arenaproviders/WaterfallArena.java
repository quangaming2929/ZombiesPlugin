package io.github.zap.zombiesplugin.gamecreator.arenaproviders;

import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.gamecreator.IArenaProvider;
import io.github.zap.zombiesplugin.manager.GameManager;
import org.bukkit.entity.Player;

/**
 * TODO: not implemented due to lack of funding and no testing facility
 */
public class WaterfallArena implements IArenaProvider {
    @Override
    public GameManager createGame(GameRoom room) {
        return  null;
    }

    @Override
    public boolean wrapPlayer(GameRoom room, Player player) {
        return false;
    }
}
