package pl.apostaremczak.aoc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class Day09 extends PuzzleSolution {
    public Day09(String inputFilename) {
        super(inputFilename);
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day09 day = new Day09("src/main/resources/09.txt");

        long startPart1 = System.currentTimeMillis();
        Long part1Solution = day.solvePart1();
        long endPart1 = System.currentTimeMillis();
        System.out.println("Part 1: " + part1Solution + " (Time: " + (endPart1 - startPart1) + " ms)");

        long startPart2 = System.currentTimeMillis();
        Long part2Solution = day.solvePart2();
        long endPart2 = System.currentTimeMillis();
        System.out.println("Part 2: " + part2Solution + " (Time: " + (endPart2 - startPart2) + " ms)");

        long endTotal = System.currentTimeMillis();
        System.out.println("Total time: " + (endTotal - startTotal) + " ms");

    }

    @Override
    public Long solvePart1() {
        Filesystem system = new Filesystem(this.fullRawInput);
        system.compact();
        return system.calculateChecksum();
    }

    @Override
    public Long solvePart2() {
        Filesystem system = new Filesystem(this.fullRawInput);
        system.compactBlocks();
        return system.calculateChecksum();
    }
}

class Filesystem {
    LinkedList<Optional<Integer>> Blocks;
    Integer HighestUnmodifiedFileId;
    Map<Integer, Integer> FileIdStartIndices;
    Map<Integer, Integer> FileIdBlockSizes;

    public Filesystem(String diskMap) {
        int fileBlockId = 0;
        boolean isFile = true;
        LinkedList<Optional<Integer>> blocks = new LinkedList<>();
        this.FileIdStartIndices = new HashMap<>();
        this.FileIdBlockSizes = new HashMap<>();

        for (char c : diskMap.toCharArray()) {
            int blockSize = Character.getNumericValue(c);

            Optional<Integer> block;
            if (isFile) {
                block = Optional.of(fileBlockId);
                this.HighestUnmodifiedFileId = fileBlockId;
                this.FileIdBlockSizes.put(fileBlockId, blockSize);
                this.FileIdStartIndices.put(fileBlockId, blocks.size());
                fileBlockId++;
            } else {
                block = Optional.empty();
            }

            for (int i = 1; i <= blockSize; i++) {
                blocks.addLast(block);
            }
            isFile = !isFile;
        }

        this.Blocks = blocks;
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        for (Optional<Integer> element : this.Blocks) {
            if (element.isPresent()) {
                representation.append(element.get());
            } else {
                representation.append(".");
            }
        }
        return representation.toString();
    }

    public void compact() {
        int currentIndex = 0;
        while (currentIndex < this.Blocks.size()) {
            Optional<Integer> element = this.Blocks.get(currentIndex);
            if (element.isPresent()) {
                currentIndex++;
                continue;
            }
            // Detect the end:
            // Only keep on removing until the last index of the block is
            // If an element is empty, get the very last non-empty element
            int lastIndex = this.Blocks.size() - 1;
            Optional<Integer> last = this.Blocks.removeLast();
            while (last.isEmpty() && lastIndex > currentIndex) {
                last = this.Blocks.removeLast();
                lastIndex--;
            }
            // Place the new element in place of this one
            if (lastIndex > currentIndex) {
                this.Blocks.remove((int) currentIndex);
                this.Blocks.add(currentIndex, last);
                currentIndex++;
            }
        }
    }

    public void compactBlocks() {
        while (this.HighestUnmodifiedFileId > 0) {
            Optional<Integer> fileBlock = Optional.of(this.HighestUnmodifiedFileId);
            // Find the number of files with this ID
            int minIndex = this.FileIdStartIndices.get(this.HighestUnmodifiedFileId);
            int fileBlockSize = this.FileIdBlockSizes.get(this.HighestUnmodifiedFileId);
//            int minIndex = this.Blocks.indexOf(fileBlock);
//            int fileBlockSize = this.Blocks.lastIndexOf(fileBlock) - minIndex + 1;
//            System.out.println("Block of " + fileBlock.get() + " of size " + fileBlockSize);
            // TODO: The index of the string will be different than the index of the actual file for double digit values!
            // e.g. '10' at index 2 will shift everything right
            // file block size = (max index - min index + 1) * file ID length! (id: 122424 occupies more space?)
            int emptyBlockIndexStart = this.indexOfFreeSpace(fileBlockSize);
            // Check if there are a
            if (emptyBlockIndexStart > -1 && (emptyBlockIndexStart + fileBlockSize) <= minIndex) {
//                System.out.println("Found an empty spot at " + emptyBlockIndexStart);

                for (int indexShift = 0; indexShift < fileBlockSize; indexShift++) {
//                    System.out.println("Moving " + fileBlock.get() + " to position " + (emptyBlockIndexStart + indexShift));
                    this.Blocks.set(emptyBlockIndexStart + indexShift, fileBlock);
                    this.Blocks.set(minIndex + indexShift, Optional.empty());
                }
            }
//            System.out.println(this.toString());
            this.HighestUnmodifiedFileId--;
        }
    }

    public Integer indexOfFreeSpace(Integer size) {
        int index = 0;
        int freeSpaceBlockSize = 0;
        for (Optional<Integer> element : this.Blocks) {
            if (element.isPresent()) {
                freeSpaceBlockSize = 0;
            } else {
                freeSpaceBlockSize++;
                if (freeSpaceBlockSize == size) {
                    return index - size + 1;
                }
            }
            index++;
        }
        return -1;
    }

    public Long calculateChecksum() {
        long checksum = 0;
        int index = 0;
        for (Optional<Integer> block : this.Blocks) {
            long element = block.orElse(0);
            checksum += (index * element);
            index++;
        }
        return checksum;
    }
}
