package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.meele.MeeleWeapon;

public abstract class MeeleSkill {
    protected final MeeleWeapon weapon;

    protected MeeleSkill(MeeleWeapon weapon) {
        this.weapon = weapon;
    }

    public abstract boolean canPerform();
    public abstract void perform();

    public MeeleWeapon getWeapon() {
        return weapon;
    }
}
