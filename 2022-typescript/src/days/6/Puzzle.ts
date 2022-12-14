import Puzzle from '../../types/AbstractPuzzle';
import { toSlidingWindows } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    // Find the index of the first four characters that are all different
    private detectStartOfPacket(buffer: string, uniqueCount = 4): number {
        const windows = toSlidingWindows<string>(Array.from(buffer), uniqueCount);
        const firstUnique = windows.findIndex(substring => {
            return new Set(substring).size == uniqueCount;
        });
        return firstUnique + uniqueCount;
    }

    public solveFirst(input: string): string {
        const inputBuffers = input.split('\n');
        const startOfPackets = inputBuffers.map(b => {
            return this.detectStartOfPacket(b);
        });
        return startOfPackets.join(' ');
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '7 5 6 10 11';
    }

    public solveSecond(input: string): string {
        const inputBuffers = input.split('\n');
        const startOfPackets = inputBuffers.map(b => {
            return this.detectStartOfPacket(b, 14);
        });
        return startOfPackets.join(' ');
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '19 23 23 29 26';
    }
}
