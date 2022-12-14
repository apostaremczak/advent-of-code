import Puzzle from '../../types/AbstractPuzzle';
import { rangeInclusive, toSlidingWindows } from '../../utils/arrays';

type X = number;
type Y = number;

const Air = '.';
const Rock = '#';
const Sand = 'o';
const Source = '+';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const cave = new Cave(input);
        while (!cave.getIsFullyAtRest()) {
            cave.pourSand();
        }
        cave.print();
        return cave.getSandCount().toString();
    }

    public getFirstExpectedResult(): string {
        return '24';
    }

    public solveSecond(input: string): string {
        const cave = new Cave(input, true);
        while (!cave.getIsFullyAtRest()) {
            cave.pourSand();
        }
        return cave.getSandCount().toString();
    }

    public getSecondExpectedResult(): string {
        return '93';
    }

}

class Cave {
    private map: Map<string, string> = new Map<string, string>();
    private minX: X = null;
    private maxX: X = null;
    private minY: Y = null;
    private maxY: Y = null;
    private isFullyAtRest = false;
    private readonly floorY: Y = null;

    constructor(input: string, withFloor = false) {
        input.split('\n').forEach(line => {
            const edges = line.split(' -> ');
            toSlidingWindows(edges, 2).forEach(([start, end], _) => {
                const [xStart, yStart] = start.split(',');
                const [xEnd, yEnd] = end.split(',');
                if (xStart === xEnd) {
                    // Build a vertical rock wall
                    rangeInclusive(Number(yStart), Number(yEnd)).forEach((y, _) => {
                        this.set(Number(xStart), y, Rock);
                    });
                } else if (yStart === yEnd) {
                    // Build a horizontal vertical wall
                    rangeInclusive(Number(xStart), Number(xEnd)).forEach((x, _) => {
                        this.set(x, Number(yStart), Rock);
                    });
                } else {
                    throw new Error(`Trying to build a diagonal wall: ${start} -> ${end}`);
                }
            });
        });

        // Add the source of sand
        this.set(500, 0, Source);

        // Add the floor if required
        if (withFloor) {
            const floorLevel = this.maxY + 2;
            this.maxY = floorLevel;
            this.floorY = floorLevel;
        }
    }


    private numsToCoords(x: number, y: number): string {
        return [x, y].join(',');
    }

    private updateBoundaries(x: number, y: number): void {
        if (this.minX === null || x < this.minX) {
            this.minX = x;
        }
        if (this.maxX === null || x > this.maxX) {
            this.maxX = x;
        }
        if (this.minY === null || y < this.minY) {
            this.minY = y;
        }
        if (this.maxY === null || y > this.maxY) {
            this.maxY = y;
        }
    }

    public set(x: number, y: number, value: string): void {
        this.updateBoundaries(x, y);
        this.map.set(this.numsToCoords(x, y), value);
    }

    public get(x: number, y: number): string {
        if (this.floorY !== null && y === this.floorY) {
            return Rock;
        }
        return this.map.get(this.numsToCoords(x, y)) || Air;
    }

    public print(): void {
        const rows = rangeInclusive(this.minY, this.maxY).map((y, _) => {
            const row = rangeInclusive(this.minX, this.maxX).map((x, _) => {
                return this.get(x, y);
            });
            return row.join('');
        });
        const representation = rows.join('\n');
        console.log(representation);
    }

    public getSandCount(): number {
        return Array.from(this.map.values()).filter(v => v === Sand).length;
    }

    public getIsFullyAtRest(): boolean {
        return this.isFullyAtRest;
    }

    public pourSand(): void {
        let currX = 500;
        let currY = 0;
        let isAtRest = false;
        let hasFallenIntoTheAbyss = false;
        while (!isAtRest && !hasFallenIntoTheAbyss) {
            if ((currX > this.maxX || currY > this.maxY || currX < this.minX) && this.floorY === null) {
                // Detect falling into the abyss, without the floor
                hasFallenIntoTheAbyss = true;
                this.isFullyAtRest = true;
            } else if (this.floorY !== null && this.get(500, 0) === Sand) {
                // Detect falling into the abyss, with the floor
                hasFallenIntoTheAbyss = true;
                this.isFullyAtRest = true;
                isAtRest = true;
            } else if (this.get(currX, currY + 1) === Air) {
                // Fall down one step if possible
                currY += 1;
            } else if (this.get(currX - 1, currY + 1) === Air) {
                // Move diagonally one step down and to the left
                currX -= 1;
                currY += 1;
            } else if (this.get(currX + 1, currY + 1) === Air) {
                // Move diagonally one step down and to the right
                currX += 1;
                currY += 1;
            } else {
                // The sand is at rest
                isAtRest = true;
            }
        }

        if (isAtRest) {
            this.set(currX, currY, Sand);
        }
    }
}
