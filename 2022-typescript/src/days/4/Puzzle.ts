import Puzzle from '../../types/AbstractPuzzle';
import { CleaningRange, createCleaningRange } from './CleaningRange';

export default class ConcretePuzzle extends Puzzle {
    private getCleaningPairs(input: string): CleaningRange[][] {
        return input.split('\n')
            .map(p => p.split(','))
            .map(([first, second]) => {
                return [createCleaningRange(first), createCleaningRange(second)];
            });
    }

    public solveFirst(input: string): string {
        const cleaningPairs: CleaningRange[][] = this.getCleaningPairs(input);

        const fullyOverlapping = cleaningPairs
            .filter(([first, second]) => {
                return first.isFullyContainedWithin(second) || second.isFullyContainedWithin(first);
            });

        return fullyOverlapping.length.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '2';
    }

    public solveSecond(input: string): string {
        const cleaningPairs: CleaningRange[][] = this.getCleaningPairs(input);
        
        const overlapping = cleaningPairs
            .filter(([first, second]) => {
                return first.overlaps(second);
            });
        return overlapping.length.toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '4';
    }
}
