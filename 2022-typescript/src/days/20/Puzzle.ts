import Puzzle from '../../types/AbstractPuzzle';
import File from './File';
import { sum } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const file = new File(input);
        file.mix();
        return sum(file.getGroveCoordinates()).toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '3';
    }

    public solveSecond(input: string): string {
        // WRITE SOLUTION FOR TEST 2
        return 'day 1 solution 2';
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return 'day 1 solution 2';
    }
}


