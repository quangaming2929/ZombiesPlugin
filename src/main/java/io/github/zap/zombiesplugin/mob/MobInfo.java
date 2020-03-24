package io.github.zap.zombiesplugin.mob;

import org.bukkit.entity.EntityType;

import java.util.Objects;

public class MobInfo {
    public final String name;
    public final int health;
    public final int movementSpeed;

    public MobInfo(String name, int health, int movementSpeed) {
        this.name = name;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }
}
