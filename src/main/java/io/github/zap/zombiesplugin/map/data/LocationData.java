package io.github.zap.zombiesplugin.map.data;

import org.bukkit.Location;

public class LocationData {
    public String worldName;
    public double x;
    public double y;
    public double z;

    public LocationData() {}

    public LocationData(Location from) {
        this.worldName = from.getWorld().getName();
        this.x = from.getX();
        this.y = from.getY();
        this.z = from.getZ();
    }
}
