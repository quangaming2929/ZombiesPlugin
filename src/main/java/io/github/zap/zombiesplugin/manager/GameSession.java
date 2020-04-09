package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.gamecreator.GameRoom;
import io.github.zap.zombiesplugin.scoreboard.IInGameScoreboard;

/**
 * Contains information about the game config. Ex: Scoreboards, Hooks, Room,...
 */
public class GameSession {
    public final GameRoom room;
    public IInGameScoreboard scoreboard;
    public GameManagerHook hook;

    public GameSession(GameRoom room) {
        this.room = room;
    }
}
