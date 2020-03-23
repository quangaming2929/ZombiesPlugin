package io.github.zap.zombiesplugin.mob;

import org.bukkit.entity.EntityType;

import java.util.Objects;

public class MobInfo {
    public final String name;
    public final int health;
    public final int movementSpeed;

    public MobInfo(String name,int health, int movementSpeed) {
        this.name = name;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobInfo mobInfo = (MobInfo) o;
        return health == mobInfo.health &&
                movementSpeed == mobInfo.movementSpeed &&
                name.equals(mobInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, health, movementSpeed);
    }
}
