package io.github.zap.zombiesplugin.utils;

import java.util.Iterator;

public interface ResettableIterator<T> extends Iterator<T> {
    void reset();
}
