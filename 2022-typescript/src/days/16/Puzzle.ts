import Puzzle from '../../types/AbstractPuzzle';
import { permute } from '../../utils/arrays';
import Valve, { ValveFactory } from './Valve';

export default class ConcretePuzzle extends Puzzle {
    private valveVFactory = new ValveFactory();

    public solveFirst(input: string): string {
        const valves: Map<string, Valve> = new Map(
            input.split('\n').map(line => {
                const valve = this.valveVFactory.getValve(line);
                return [valve.name, valve];
            })
        );
        const nonZeroValves = Array.from(valves.values()).filter(v => v.flowRate > 0).map(v => v.name);

        // TODO: Find the shortest paths between all sensible (on-zero) valves
        // Use Floyd-Warshall Algorithm O(n^3) - https://www.programiz.com/dsa/floyd-warshall-algorithm

        // TODO: Cache and solve with DP
        // TODO: "If I'm at node X, and have T minutes left, how much pressure can I still release, ignoring already opened valves?"
        // https://www.reddit.com/r/adventofcode/comments/zo21au/2022_day_16_approaches_and_pitfalls_discussion/

        //     Calculate the distances between all valves (use a distance matrix and fill it - that's essentially Floyd-Warshall)
        //     In your recursive method (or DP step), don't ask "which neighbor valve will I visit next?", but "which non-broken valve will I OPEN next?"
        return 'day 1 solution 1';
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '1651';
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
