package io.github.zap.zombiesplugin.data.ultvalue;

import io.github.zap.zombiesplugin.data.IIncludeLore;

import java.util.Locale;

/**
 * A simple object present a equipment value that will be display on
 * the equipment lore
 */
public class LoreEquipmentValue extends EquipmentValue implements IIncludeLore {
    public String unit;

    public LoreEquipmentValue() {
    }

    public LoreEquipmentValue(float value) {
        super(value);
    }

    public LoreEquipmentValue(float value, String unit) {
        super(value);
        this.unit = unit;
    }

    public LoreEquipmentValue(float value, String unit, int precision) {
        super(value);
        this.unit = unit;
        this.precision = precision;
    }

    /**
     * The precision after decimal point
     */
    public int precision = 0;

    @Override
    public String getDisplayValue() {
        return String.format(Locale.US, "%." + precision + "f", value) + " " + unit;
    }
}
