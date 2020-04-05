package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.spawn.SpawnFilter;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

import java.util.ArrayList;
import java.util.HashSet;

public class SpawnFilterData implements IMapData<SpawnFilter> {
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
}
