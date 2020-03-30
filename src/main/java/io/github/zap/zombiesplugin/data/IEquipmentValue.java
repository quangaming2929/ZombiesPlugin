package io.github.zap.zombiesplugin.data;

import io.github.zap.zombiesplugin.provider.ICustomSerializerIdentity;

public interface IEquipmentValue extends ICustomSerializerIdentity {
    float provideValue();
}
