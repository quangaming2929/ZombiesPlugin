package io.github.zap.zombiesplugin.equipments.perks;

import io.github.zap.zombiesplugin.data.EquipmentData;
import io.github.zap.zombiesplugin.manager.UserManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPerk extends Perk {
    public static final String PERK_SPEED = "speed";

    protected boolean isEquipped = false;

    public SpeedPerk(EquipmentData equipmentData, UserManager userManager) {
        super(equipmentData, userManager);
    }

    @Override
    public void onEquip() {
        int speedLevel = (int)tryGetValue(PERK_SPEED) - 1;
        applyEffect(speedLevel);

        isEquipped = true;
    }

    private void applyEffect(int speedLevel) {
        // Check for already existing speed value
        PotionEffect speedEf = getPlayer().getPotionEffect(PotionEffectType.SPEED);
        if (speedEf != null) {
            speedLevel += 1 + speedEf.getAmplifier();
        }

        if (speedLevel >= 0) {
            getPlayer().removePotionEffect(PotionEffectType.SPEED);
            speedEf = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel, false, false);
            getPlayer().addPotionEffect(speedEf);
        }
    }

    @Override
    public void setLevel(int level) {
        // represent the different between 2 level
        int finLevel = 0;
        if (isEquipped) {
            finLevel = - (int) tryGetValue(PERK_SPEED);
        }

        super.setLevel(finLevel);
        finLevel += (int) tryGetValue(PERK_SPEED);
        applyEffect(finLevel);
    }

    @Override
    public void onUnEquip() {
        int speedLevel = (int) tryGetValue(PERK_SPEED) - 1;

        if (speedLevel > 0) {
            applyEffect(-speedLevel);
        }

        isEquipped = false;
    }
}
