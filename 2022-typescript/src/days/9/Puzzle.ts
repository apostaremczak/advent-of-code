import Puzzle from '../../types/AbstractPuzzle';
import { deduplicate } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    private Moves: Map<string, [number, number]> = new Map([
        ['U', [1, 0]],
        ['D', [-1, 0]],
        ['L', [0, -1]],
        ['R', [0, 1]]
    ]);

    private areTouching(first: RopeEnd, second: RopeEnd): boolean {
        return Math.abs(first.x - second.x) <= 1 && Math.abs(first.y - second.y) <= 1;
    }

    public solveFirst(input: string): string {
        const head = new RopeEnd();
        const tail = new RopeEnd();
        const visitedByTail: string[] = [];

        input.split('\n').map(instruction => {
            const [direction, amountStr] = instruction.split(' ');
            Array.from(Array(parseInt(amountStr))).forEach(_ => {
                head.move(this.Moves.get(direction));

                if (!this.areTouching(head, tail)) {
                    tail.move([Math.sign(head.x - tail.x), Math.sign(head.y - tail.y)]);
                }

                visitedByTail.push([tail.x, tail.y].join(','));
            });
        });

        return deduplicate(visitedByTail).length.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '13';
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

class RopeEnd {
    public x: number;
    public y: number;

    constructor() {
        this.x = 0;
        this.y = 0;
    }

    public move(direction: [number, number]): void {
        const [x, y] = direction;
        this.x += x;
        this.y += y;
    }
}
