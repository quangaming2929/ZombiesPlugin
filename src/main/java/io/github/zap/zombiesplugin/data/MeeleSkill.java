package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.equipments.meele.MeleeWeapon;

public abstract class MeeleSkill {
    protected final MeleeWeapon weapon;

    protected MeeleSkill(MeleeWeapon weapon) {
        this.weapon = weapon;
    }

    public abstract boolean canPerform();
    public abstract void perform();

    public MeleeWeapon getWeapon() {
        return weapon;
    }
}
