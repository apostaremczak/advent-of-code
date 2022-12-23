import Puzzle from '../../types/AbstractPuzzle';
import { RockTetris } from './RockTetris';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const moves = this.parseMoves(input);
        const rockTetris = new RockTetris(moves);
        const rockCount = 2022;
        let next = {
            cycleIdx: 1, gasMoveIdx: 0, figureIdx: 0
        };
        let i = next.cycleIdx;

        while (i <= rockCount) {
            console.log(`----------${i}-------`);
            next = rockTetris.simulateWithCache(next.cycleIdx, next.gasMoveIdx, next.figureIdx);
            rockTetris.seed(next.gasMoveIdx, next.figureIdx);
            // TODO: zle przeskakuje i seeduje pod koniec
            if (next.cycleIdx > rockCount) {
                // Go slowly one by one
                rockTetris.dropNewRock();
                i += 1;
            } else {
                i = next.cycleIdx;
            }
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
        let next = {
            cycleIdx: 1, gasMoveIdx: 0, figureIdx: 0
        };

        while (next.cycleIdx <= rockCount) {
            next = rockTetris.simulateWithCache(next.cycleIdx, next.gasMoveIdx, next.figureIdx);
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
