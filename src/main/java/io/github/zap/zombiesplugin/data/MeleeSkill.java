package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.equipments.meele.MeleeWeapon;
import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;

public abstract class MeleeSkill implements ICustomSerializerIdentity {
    protected final MeleeWeapon weapon;

    protected MeleeSkill(MeleeWeapon weapon) {
        this.weapon = weapon;
    }

    public abstract boolean canPerform();
    public abstract void perform();

    public MeleeWeapon getWeapon() {
        return weapon;
    }
}
