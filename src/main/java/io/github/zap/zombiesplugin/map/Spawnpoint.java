package io.github.zap.zombiesplugin.map;

import org.bukkit.util.Vector;

public abstract class Spawnpoint {
    public Vector coordinates;

    public abstract void spawn();
}
