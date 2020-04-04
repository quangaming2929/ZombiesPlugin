package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.ZombiesPlugin;
import org.bukkit.Location;

public class LocationData implements IMapData<Location> {
    public String worldName;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    public LocationData() {}

    public LocationData(Location from) {
        worldName = from.getWorld().getName();
        x = from.getX();
        y = from.getY();
        z = from.getZ();
        pitch = from.getPitch();
        yaw = from.getYaw();
    }

    @Override
    public Location load() {
        return new Location(ZombiesPlugin.instance.getServer().getWorld(worldName), x, y, z, pitch, yaw);
    }
}
