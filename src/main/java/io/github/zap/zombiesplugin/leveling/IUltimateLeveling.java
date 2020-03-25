package io.github.zap.zombiesplugin.leveling;

import io.github.zap.zombiesplugin.guns.data.*;
import io.github.zap.zombiesplugin.provider.*;

public interface IUltimateLeveling extends ICustomSerializerIdentity {
    BulletStats getLevel(int level);
    int size();
}
