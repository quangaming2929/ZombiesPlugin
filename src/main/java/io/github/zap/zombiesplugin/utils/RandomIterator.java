package io.github.zap.zombiesplugin.utils;

import java.util.*;

public class RandomIterator<T> implements ResettableIterator<T> {
    private static final Random RNG = new Random();
    private int[] order;
    private final List<T> elements;
    private int currentIndex = 0;

    public RandomIterator(List<T> elements) {
        this.elements = elements;
        this.order = generateRandomOrder(elements.size());
    }

    private int[] generateRandomOrder(int size) {
        int[] index = new int[size];
        for (int i = 0; i < size; i++) {
            index[i] = i;
        }

        int swap;
        for (int i = 0; i < size; i++) {
            int randomIndex = getRandomInt(0, size);
            swap = index[i];
            index[i] = index[randomIndex];
            index[randomIndex] = swap;
        }

        return index;
    }


    private int getRandomInt(int lowerBound, int upperBound) {
        return RNG.nextInt(upperBound - lowerBound) + lowerBound;
    }

    @Override
    public boolean hasNext() {
        return currentIndex != elements.size();
    }

    @Override
    public T next() {
        return elements.get(order[currentIndex++]);
    }

    public void reset() {
        order = generateRandomOrder(elements.size());
    }
}
