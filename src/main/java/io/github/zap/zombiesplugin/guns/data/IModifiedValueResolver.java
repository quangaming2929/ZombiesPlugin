package io.github.zap.zombiesplugin.guns.data;

public interface IModifiedValueResolver {
    String addModifier( String name, String baseValue, float modifier );
}
