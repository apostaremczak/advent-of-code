import Puzzle from '../../types/AbstractPuzzle';
import { deduplicate } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    private Moves: Map<string, [number, number]> = new Map([
        ['U', [1, 0]],
        ['D', [-1, 0]],
        ['L', [0, -1]],
        ['R', [0, 1]]
    ]);

    public solveFirst(input: string): string {
        const tail = new RopeKnot();
        const head = new RopeKnot(tail);

        input.split('\n').map(instruction => {
            const [direction, amountStr] = instruction.split(' ');
            Array.from(Array(parseInt(amountStr))).forEach(_ => {
                head.move(this.Moves.get(direction));
            });
        });

        return tail.getVisitedSpotsCount().toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '13';
    }

    public solveSecond(input: string): string {
        const tail = new RopeKnot();
        const eighth = new RopeKnot(tail);
        const seventh = new RopeKnot(eighth);
        const sixth = new RopeKnot(seventh);
        const fifth = new RopeKnot(sixth);
        const fourth = new RopeKnot(fifth);
        const third = new RopeKnot(fourth);
        const second = new RopeKnot(third);
        const first = new RopeKnot(second);
        const head = new RopeKnot(first);

        input.split('\n').map(instruction => {
            const [direction, amountStr] = instruction.split(' ');
            Array.from(Array(parseInt(amountStr))).forEach(_ => {
                head.move(this.Moves.get(direction));
            });
        });

        return tail.getVisitedSpotsCount().toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '1';
    }


}

class RopeKnot {
    private x: number;
    private y: number;
    private visitedSpots: string[];

    constructor(readonly next?: RopeKnot) {
        this.x = 0;
        this.y = 0;
        this.visitedSpots = [];
        this.markAsVisited();
    }

    private markAsVisited(): void {
        this.visitedSpots.push([this.x, this.y].join(','));
    }

    public move(direction: [number, number]): void {
        const [x, y] = direction;
        this.x += x;
        this.y += y;

        if (this.next !== undefined && !this.isTouching(this.next)) {
            this.next.move([
                Math.sign(this.x - this.next.x),
                Math.sign(this.y - this.next.y)
            ]);
        }

        this.markAsVisited();
    }

    private isTouching(other: RopeKnot): boolean {
        return Math.abs(this.x - other.x) <= 1 && Math.abs(this.y - other.y) <= 1;
    }

    public getVisitedSpotsCount(): number {
        return deduplicate(this.visitedSpots).length;
    }
}
