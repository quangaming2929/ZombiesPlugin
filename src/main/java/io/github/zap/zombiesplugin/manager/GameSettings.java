package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.GameMap;

public class GameSettings {
    private GameDifficulty difficulty;
    private GameMap gameMap;
    private int gameSize;

    public GameSettings(GameDifficulty difficulty, GameMap map, int size) {
        this.difficulty = difficulty;
        this.gameMap = map;
        gameSize = size;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getGameSize() {
        return gameSize;
    }
}
