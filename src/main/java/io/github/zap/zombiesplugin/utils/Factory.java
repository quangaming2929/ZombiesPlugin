package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.guns.data.GunData;
import io.github.zap.zombiesplugin.guns.data.IModifiedValueResolver;
import io.github.zap.zombiesplugin.guns.data.ModifiableBulletStats;

/**
 * Use this class as a simple Dependency Injection
 */
public class Factory {
    public static ModifiableBulletStats GetModifableBulletStats(IModifiedValueResolver resolver, GunData data){
        return new ModifiableBulletStats(data.stats, resolver);
    }
}
