package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.manager.GameManager;

public interface ICost {
    int getCost(GameManager currentGame, int boughtCount);
}
