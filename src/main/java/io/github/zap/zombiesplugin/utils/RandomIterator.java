package io.github.zap.zombiesplugin.utils;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class RandomIterator<T> implements Iterator<T> {
    int i;
    int n;
    List<T> list;
    HashMap<Integer, T> shuffled ;
    Random rng;

    public RandomIterator(List<T> list) {
        i = 0;
        n = list.size();
        this.list = list;
        rng = new Random();
        shuffled = new HashMap<>();
    }

    public boolean hasNext() { return i < n; }

    public T next() {
        int j = i + rng.nextInt(n-i);
        T a = get(i), b = get(j);

        shuffled.put(j, a);
        shuffled.remove(i);

        i++;
        return b;
    }

    @Override
    public void remove() {
        throw new NotImplementedException();
    }

    private T get(int i) {
        return shuffled.containsKey(i) ? shuffled.get(i) : list.get(i);
    }

    public void reset() {
        shuffled.clear();
        i = 0;
    }
}
