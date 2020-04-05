package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class OrderedIterator<T> implements ResettableIterator<T> {
    private Iterator<T> iterator;
    private final List<T> elements;

    public OrderedIterator(List<T> elements) {
        this.iterator = elements.iterator();
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void reset() {
        iterator = elements.iterator();
    }
}
