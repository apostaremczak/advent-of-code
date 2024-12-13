package pl.apostaremczak.aoc.util;

import java.util.HashSet;
import java.util.Set;

public interface SubsetSupport {
    static <T> Set<Set<T>> getAllPairs(Set<T> set) {
        Set<Set<T>> pairs = new HashSet<>();
        for (T a1 : set) {
            for (T a2 : set) {
                if (!a1.equals(a2)) {
                    pairs.add(Set.of(a1, a2));
                }
            }
        }

        return pairs;
    }
}
