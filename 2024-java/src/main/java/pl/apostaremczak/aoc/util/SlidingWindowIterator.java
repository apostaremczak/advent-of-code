package pl.apostaremczak.aoc.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SlidingWindowIterator<T> implements Iterator<List<T>> {
    private final List<T> list;
    private final int windowSize;
    private int currentIndex;

    public SlidingWindowIterator(List<T> list, int windowSize) {
        this.list = list;
        this.windowSize = windowSize;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex <= list.size() - windowSize;
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        List<T> window = list.subList(currentIndex, currentIndex + windowSize);
        currentIndex++;
        return window;
    }
}
