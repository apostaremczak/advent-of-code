import Puzzle from '../../types/AbstractPuzzle';
import { parseBlueprints } from './Blueprint';
import { sum } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const blueprints = parseBlueprints(input);
        const qualityLevels = blueprints.map(b => b.getQualityLevel());
        return sum(qualityLevels).toString();
    };

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '33';
    }

    public solveSecond(input: string): string {
        const blueprints = parseBlueprints(input, 32).slice(0, 3);
        const maxGeodes = blueprints.map(b => b.findMaxGeodes());
        return maxGeodes.reduce((p, n) => p * n, 1).toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return (56 * 62).toString();
    }
}
