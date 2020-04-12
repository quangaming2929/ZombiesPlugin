package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.Door;
import io.github.zap.zombiesplugin.utils.Tuple;

public class DoorData implements IMapData<Door>, IEditorContext {
    public DoorData() {}

    public DoorData(Door from) {
        //TODO: implement this
    }

    @Override
    public Door load(Object args) {
        return null;
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String name, String[] args) {
        return null;
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {

    }
}
