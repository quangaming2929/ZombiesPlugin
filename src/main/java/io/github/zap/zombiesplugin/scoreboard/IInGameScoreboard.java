package io.github.zap.zombiesplugin.scoreboard;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.manager.GameState;
import io.github.zap.zombiesplugin.player.User;

public interface IInGameScoreboard {
    GameManager getManager();

    void onPlayerJoinGame(User user);
    void onPlayerLeave(User user);
    void setPlayerCount(int count);
    void setGameState(GameState state);
    void invalidateScoreboards ();

    // In game
    void setZombiesCount(int count);
    void setRound(int round);
}
