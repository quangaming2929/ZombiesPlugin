package io.github.zap.zombiesplugin.guns.data.leveling;

import io.github.zap.zombiesplugin.guns.data.BulletStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent gun levels in a list form
 */
public class UltimateLevelingList implements IUltimateLeveling{

    public List<BulletStats> levels;

    @Override
    public BulletStats getLevel(int level) {
        return  levels.get(level);
    }

    @Override
    public int size() {
        return levels.size();
    }
}
