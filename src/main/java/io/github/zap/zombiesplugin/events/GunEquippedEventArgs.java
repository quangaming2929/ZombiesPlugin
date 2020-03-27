package io.github.zap.zombiesplugin.events;

import io.github.zap.zombiesplugin.guns.Gun;

public class GunEquippedEventArgs extends EventArgs {
    public final Gun affectedGun;

    public GunEquippedEventArgs(Gun affectedGun) {
        this.affectedGun = affectedGun;
    }
}
