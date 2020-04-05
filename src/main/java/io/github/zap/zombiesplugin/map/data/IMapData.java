package io.github.zap.zombiesplugin.map.data;

public interface IMapData<T> {
    T load(Object args);
}
