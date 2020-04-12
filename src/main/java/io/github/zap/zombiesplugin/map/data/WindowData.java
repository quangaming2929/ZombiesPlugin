package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.Room;
import io.github.zap.zombiesplugin.map.Window;
import io.github.zap.zombiesplugin.utils.Tuple;
import org.bukkit.Material;

public class WindowData implements IMapData<Window>, IEditorContext {
    public SpawnPointData spawnPoint;
    public MultiBoundingBoxData interiorBounds;
    public BoundingBoxData windowBounds;
    public String coverMaterial;

    private transient RoomData parent;

    public WindowData() {}

    public WindowData(Window from, RoomData parent) {
        this.parent = parent;
        spawnPoint = new SpawnPointData(from.getSpawnpoint(0), this);
        interiorBounds = new MultiBoundingBoxData(from.getInteriorBounds());
        windowBounds = new BoundingBoxData(from.getWindowBounds());
        coverMaterial = from.getCoverMaterial().name();
    }

    @Override
    public Window load(Object args) {
        if(args instanceof Room) {
            Window window = new Window(windowBounds.load(null), interiorBounds.load(null), Material.getMaterial(coverMaterial), (Room)args);
            window.add(spawnPoint.load(window));
            return window;
        }
        else {
            throw new IllegalArgumentException("args must be a Room instance");
        }
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String name, String[] args) {
        return null;
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {

    }
}
