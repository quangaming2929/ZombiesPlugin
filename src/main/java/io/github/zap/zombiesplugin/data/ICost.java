package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;

public interface ICost extends ICustomSerializerIdentity {
    int getCost(GameManager currentGame, int boughtCount);
}
