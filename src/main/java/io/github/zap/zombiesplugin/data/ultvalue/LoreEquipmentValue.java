package io.github.zap.zombiesplugin.data.ultvalue;

import io.github.zap.zombiesplugin.data.IIncludeLore;

import java.util.Locale;

/**
 * A simple object present a equipment value that will be display on
 * the equipment lore
 */
public class LoreEquipmentValue extends EquipmentValue implements IIncludeLore {
    public String unit;

    /**
     * The precision after decimal point
     */
    public int precision = 1;

    @Override
    public String getDisplayValue() {
        return String.format(Locale.US, "%." + precision + "f", value) + " " + unit;
    }
}
