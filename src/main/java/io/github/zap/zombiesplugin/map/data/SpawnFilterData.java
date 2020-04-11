package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.commands.mapeditor.ContextManager;
import io.github.zap.zombiesplugin.commands.mapeditor.IEditorContext;
import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.github.zap.zombiesplugin.utils.Tuple;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;

public class SpawnFilterData implements IMapData<SpawnFilter>, IEditorContext {
    public ArrayList<String> mobNames;

    public SpawnFilterData() {}

    public SpawnFilterData(SpawnFilter from) {
        mobNames = new ArrayList<>();
        for(MythicMob mob : from.getAcceptedMobTypes()) {
            mobNames.add(mob.getInternalName());
        }
    }

    @Override
    public SpawnFilter load(Object args) {
        HashSet<MythicMob> result = new HashSet<>();
        for(String name : mobNames) {
            result.add(MythicMobs.inst().getAPIHelper().getMythicMob(name));
        }

        return new SpawnFilter(result);
    }

    @Override
    public Tuple<Boolean, String> canExecute(ContextManager session, String name, String[] args) {
        return null;
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {

    }
}
