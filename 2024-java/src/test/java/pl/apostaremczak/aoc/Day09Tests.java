package pl.apostaremczak.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09Tests {
    private final Day09 day09 = new Day09("src/test/resources/09.txt");

    @Test
    public void testSolvePart1() {
        Long result = day09.solvePart1();
        assertEquals(1928L, result);
    }

    @Test
    public void testSolvePart2() {
        Long result = day09.solvePart2();
        assertEquals(2858L, result);
    }

    @Test
    public void testFilesystem() {
        Filesystem system = new Filesystem("12345");
        assertEquals("0..111....22222", system.toString());

        Filesystem system1 = new Filesystem(day09.fullRawInput);
        assertEquals("00...111...2...333.44.5555.6666.777.888899", system1.toString());
    }

    @Test
    public void testFilesystemCompact() {
        Filesystem system = new Filesystem("12345");
        system.compact();
        assertEquals("022111222", system.toString());

        Filesystem system1 = new Filesystem(day09.fullRawInput);
        system1.compact();
        assertEquals("0099811188827773336446555566", system1.toString());
    }

    @Test
    public void testFilesystemChecksum() {
        Filesystem system = new Filesystem("12345");
        system.compactBlocks();
        assertEquals(132L, system.calculateChecksum());

        Filesystem system1 = new Filesystem(day09.fullRawInput);
        system1.compact();
        assertEquals(1928L, system1.calculateChecksum());

        Filesystem system2 = new Filesystem("1010101010101010101010");
        system2.compactBlocks();
        assertEquals(385L, system2.calculateChecksum());

        Filesystem system3 = new Filesystem("12101");
        system3.compactBlocks();
        assertEquals(4L, system3.calculateChecksum());

        Filesystem system4 = new Filesystem("80893804751608292");
        system4.compactBlocks();
        assertEquals(1715L, system4.calculateChecksum());

        Filesystem system5 = new Filesystem("12101");
        system5.compactBlocks();
        assertEquals(4L, system5.calculateChecksum());

        Filesystem system6 = new Filesystem("233313312141413140211");
        system6.compactBlocks();
        assertEquals(2910L, system6.calculateChecksum());
    }

    @Test
    public void testFilesystemCompactBlocks() {
        Filesystem system1 = new Filesystem(day09.fullRawInput);
        system1.compactBlocks();
        assertEquals("00992111777.44.333....5555.6666.....8888..", system1.toString());
    }
}
