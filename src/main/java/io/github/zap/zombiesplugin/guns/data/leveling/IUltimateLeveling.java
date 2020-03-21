package io.github.zap.zombiesplugin.guns.data.leveling;

import io.github.zap.zombiesplugin.guns.data.BulletStats;
import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;

public interface IUltimateLeveling extends ICustomSerializerIdentity {
    BulletStats getLevel(int level);
    int size();
}
