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
        const file = new File(input, 811589153);
        // Mix it ten times
        Array(10)
            .fill(0)
            .forEach(_ =>
                file.mix()
            );
        return sum(file.getGroveCoordinates()).toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '1623178306';
    }
}
