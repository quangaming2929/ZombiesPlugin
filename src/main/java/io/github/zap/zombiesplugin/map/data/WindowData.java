package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.Window;
import org.bukkit.Material;

public class WindowData implements IMapData<Window> {
    public SpawnPointData spawnPoint;
    public MultiBoundingBoxData interiorBounds;
    public BoundingBoxData windowBounds;
    public String coverMaterial;

    public WindowData() {}

    public WindowData(Window from) {
        spawnPoint = new SpawnPointData(from.getSpawnpoint());
        interiorBounds = new MultiBoundingBoxData(from.getInteriorBounds());
        windowBounds = new BoundingBoxData(from.getWindowBounds());
        coverMaterial = from.getCoverMaterial().name();
    }

    @Override
    public Window load(Object args) {
        return new Window(windowBounds.load(null), interiorBounds.load(null), spawnPoint.load(null), Material.getMaterial(coverMaterial));
    }
}
