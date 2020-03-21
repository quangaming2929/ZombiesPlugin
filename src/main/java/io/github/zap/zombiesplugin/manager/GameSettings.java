package io.github.zap.zombiesplugin.manager;

import io.github.zap.zombiesplugin.map.Map;

/**
 * Currently only includes difficulty and map, but could hold data about "modes" like Endless or other things
 * we come up with.
 */
public class GameSettings {
    public GameDifficulty difficulty;
    public Map gameMap;
}
