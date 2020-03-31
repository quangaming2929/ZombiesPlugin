package io.github.zap.zombiesplugin.shop.machine.data;

import io.github.zap.zombiesplugin.manager.GameManager;

public class StaticCost implements ICost {
    public StaticCost(int cost) {
        this.cost = cost;
    }

    public StaticCost() {
    }

    public int cost;

    @Override
    public int getCost(GameManager currentGame, int boughtCount) {
        return cost;
    }
}
