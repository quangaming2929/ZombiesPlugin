package io.github.zap.zombiesplugin.mob;

public class MobInfo {
    public final String Name;
    public final int health;
    public final int movementSpeed;

    public MobInfo(String name, int health, int movementSpeed) {
        Name = name;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }
}
