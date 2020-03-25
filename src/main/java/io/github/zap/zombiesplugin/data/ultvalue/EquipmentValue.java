package io.github.zap.zombiesplugin.data.ultvalue;

import io.github.zap.zombiesplugin.data.IEquipmentValue;

/**
 * A simple object present a equipment value
 */
public class EquipmentValue implements IEquipmentValue {
    public float value;

    @Override
    public float provideValue() {
        return value;
    }
}
