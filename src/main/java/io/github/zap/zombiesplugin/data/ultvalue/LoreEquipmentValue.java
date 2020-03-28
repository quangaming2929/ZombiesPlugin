package io.github.zap.zombiesplugin.data.ultvalue;

import io.github.zap.zombiesplugin.data.IIncludeLore;

import java.util.Locale;

/**
 * A simple object present a equipment value that will be display on
 * the equipment lore
 */
public class LoreEquipmentValue extends EquipmentValue implements IIncludeLore {
    public String unit = "";
    public String name;

    public LoreEquipmentValue() {
    }

    public LoreEquipmentValue(float value) {
        super(value);
    }

    public LoreEquipmentValue(float value, String name) {
        super(value);
        this.name = name;
    }

    public LoreEquipmentValue(float value, String name, String unit) {
        super(value);
        this.unit = unit;
        this.name = name;
    }

    public LoreEquipmentValue(float value, String name, String unit, int precision) {
        super(value);
        this.unit = unit;
        this.precision = precision;
        this.name = name;
    }

    /**
     * The precision after decimal point
     */
    public int precision = 0;

    @Override
    public String getStatsName() {
        return name;
    }

    @Override
    public String getDisplayValue() {
        return String.format(Locale.US, "%." + precision + "f", value) + " " + unit;
    }
}
