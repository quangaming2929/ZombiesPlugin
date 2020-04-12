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
        switch(name) {
            case "addmob":
                if(args.length == 1) {
                    if(MythicMobs.inst().getMobManager().getMythicMob(args[0]) != null) {
                        return new Tuple<>(true, "Added mob.");
                    }
                    return new Tuple<>(true, "That mob type is not registered. It will be added to the list, but it may cause issues.");
                }
                return new Tuple<>(false, "Usage: /addmob [mob-name]");
            case "deletemob":
                if(args.length == 1) {
                    if(mobNames.contains(args[0])) {
                        return new Tuple<>(true, "Removed mob.");
                    }
                    return new Tuple<>(true, "A mob with that name does not exist.");
                }
                return new Tuple<>(false, "Usage: /removemob [mob-name]");
            default:
                return new Tuple<>(false, "This command cannot be run on a spawnpoint.");
        }
    }

    @Override
    public void execute(ContextManager session, String name, String[] args) {
        switch (name) {
            case "addmob":
                mobNames.add(args[0]);
                break;
            case "deletemob":
                mobNames.remove(args[0]);
                break;
        }
    }
}
