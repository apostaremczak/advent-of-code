import Puzzle from '../../types/AbstractPuzzle';
import { RockTetris } from './RockTetris';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const moves = this.parseMoves(input);
        const rockTetris = new RockTetris(moves);

        for (let i = 1; i <= 2022; i++) {
            rockTetris.dropNewRock();
        }
        return rockTetris.getCurrentHeight().toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '3068';
    }

    public solveSecond(input: string): string {
        const moves = this.parseMoves(input);
        const rockTetris = new RockTetris(moves, 7, 10);
        const rockCount = 1_000_000_000_000;

        for (let i = 1; i <= rockCount; i++) {
            rockTetris.dropNewRock();
        }

        return rockTetris.getCurrentHeight().toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '1514285714288';
    }

    private parseMoves(input: string): string[] {
        return input.trim().split('');
    }
}
