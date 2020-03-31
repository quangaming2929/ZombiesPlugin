package io.github.zap.zombiesplugin.events;

import io.github.zap.zombiesplugin.equipments.guns.Gun;

public class GunUnEquippedEventArgs extends EventArgs {
    public final Gun removedGun;

    public GunUnEquippedEventArgs(Gun removedGun) {
        this.removedGun = removedGun;
    }
}
