package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.ISpawnpointContainer;
import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.utils.Tuple;

public class SpawnPointData implements IMapData<SpawnPoint>, IEditorContext {
    public LocationData spawn;
    public LocationData target;

    private transient IMapData<?> parent;

    public SpawnPointData() {}

    public SpawnPointData(SpawnPoint from, IMapData<?> parent) {
        this.parent = parent;
        spawn = new LocationData(from.getSpawn());
        target = new LocationData(from.getTarget());
    }

    @Override
    public SpawnPoint load(Object args) {
        if(args instanceof ISpawnpointContainer) {
            return new SpawnPoint(spawn.load(null), target.load(null), (ISpawnpointContainer)args);
        }
        else {
            throw new IllegalArgumentException("args must implement ISpawnpointContainer");
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
