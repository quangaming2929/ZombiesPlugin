package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.GameMap;

public class GameSettings {
    public GameDifficulty difficulty;
    public GameMap gameMap;
    public int gameSize;

    public GameSettings(GameDifficulty difficulty, GameMap map, int size) {
        this.difficulty = difficulty;
        this.gameMap = map;
        gameSize = size;
    }
}
