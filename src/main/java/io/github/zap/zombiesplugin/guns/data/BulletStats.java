package io.github.zap.zombiesplugin.guns.data;

public class BulletStats {
    public double baseRange;
    public double baseDamage;
    public int baseAmmoSize;
    public int baseClipAmmoSize;
    public double baseFireRate;
    public double baseReloadRate;

    public double getRange() {
        return baseRange;
    }

    public void setRange(double range) {
        this.baseRange = range;
    }

    public double getDamage() {
        return baseDamage;
    }

    public void setDamage(double damage) {
        this.baseDamage = damage;
    }

    public int getAmmoSize() {
        return baseAmmoSize;
    }

    public void setAmmoSize(int ammoSize) {
        this.baseAmmoSize = ammoSize;
    }

    public int getClipAmmoSize() {
        return baseClipAmmoSize;
    }

    public void setClipAmmoSize(int clipAmmoSize) {
        this.baseClipAmmoSize = clipAmmoSize;
    }

    public double getFireRate() {
        return baseFireRate;
    }

    public void setFireRate(double fireRate) {
        this.baseFireRate = fireRate;
    }

    public double getReloadRate() {
        return baseReloadRate;
    }

    public void setReloadRate(double reloadRate) {
        this.baseReloadRate = reloadRate;
    }
}
