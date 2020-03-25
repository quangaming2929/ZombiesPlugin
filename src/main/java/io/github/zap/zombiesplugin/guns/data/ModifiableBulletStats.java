package io.github.zap.zombiesplugin.guns.data;

import io.github.zap.zombiesplugin.leveling.IUltimateLeveling;

import java.util.Hashtable;

/**
 * Represent a gun stats that directly called by gun logic
 * Subclass this to
 */
public class ModifiableBulletStats {
    private int level = 0;
    public double modifierRange = 1;
    public double modifierDamage = 1;
    public int modifierAmmoSize = 1;
    public int modifierClipAmmoSize = 1;
    public double modifierFireRate = 1;
    public double modifierReloadRate = 1;
    public int modifierRewardGold = 1;
    public float modifierKnockback = 1;


    public final IUltimateLeveling levels;
    public final Hashtable<String, Float> customModifiers;
    public final IModifiedValueResolver resolver;

    public ModifiableBulletStats(IUltimateLeveling levels, IModifiedValueResolver resolver) {
        this.levels = levels;
        this.resolver = resolver;
        customModifiers = new Hashtable<>();
    }

    /**
     * Get the modifier value for custom value. return 1 if it's not exist
     * @param name the custom value name
     */
    public float getCustomModifier(String name) {
        return customModifiers.containsKey(name) ? customModifiers.get(name) : 1f;
    }

    public void setCustomModifiers(String name, float value) {
        if (customModifiers.containsKey(name)) {
            customModifiers.replace(name, value);
        } else {
            customModifiers.put(name, value);
        }
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getRange() {
        return levels.getLevel(level).baseRange * modifierRange;
    }

    public double getDamage() {
        return levels.getLevel(level).baseDamage * modifierDamage;
    }

    public int getAmmoSize() {
        return levels.getLevel(level).baseAmmoSize * modifierAmmoSize;
    }

    public int getClipAmmoSize() {
        return levels.getLevel(level).baseClipAmmoSize * modifierClipAmmoSize;
    }

    public double getFireRate() {
        return levels.getLevel(level).baseFireRate * modifierFireRate;
    }

    public double getReloadRate() {
        return levels.getLevel(level).baseReloadRate * modifierReloadRate;
    }

    public int getRewardGold() {
        return levels.getLevel(level).baseRewardGold * modifierRewardGold;
    }

    public float getKnockback() {
        return levels.getLevel(level).baseKnockback * modifierKnockback;
    }

    public String getCustomValue(String name) {
        return resolver.addModifier(name, levels.getLevel(level).customValues.get(name).value, getCustomModifier(name));
    }
}