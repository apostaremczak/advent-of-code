import { Coord, coordToStr, strToCoord } from '../../utils/mazes';
import { RockFigure, RockFigureFactory } from './RockFigure';
import { MoveDown, MoveType } from './Moves';

type GameState = {
    height: number,
    rockCount: number
}

export class RockTetris {
    private grid = new Map<string, string>();
    private readonly Floor = '_';
    private readonly Wall = '|';
    private readonly Air = '.';
    private height = 0;
    private rockFactory = new RockFigureFactory();
    private gasMoveIdx = 0;
    private cache = new Map<string, GameState>();
    private cacheRowCount = 50;

    constructor(readonly gasMoves: string[], readonly width: number = 7, readonly maxHeight: number = 100) {
    }

    private getNextFigure(): RockFigure {
        // Generate a new figure - two units from the left, three units above the other figures present
        return this.rockFactory.getNextRock(2, this.height + 3);
    }

    private getFigure(index: number): RockFigure {
        return this.rockFactory.getRock(2, this.height + 3, index);
    }

    private updateHeight(): void {
        // Find the new maximum y
        this.height = Math.max(...Array.from(this.grid.keys()).map(k => strToCoord(k).y)) + 1;
    }

    public getCurrentHeight(): number {
        return this.height;
    }

    private getNextGasMove(): MoveType {
        const move = this.gasMoves[this.gasMoveIdx];
        this.gasMoveIdx = (this.gasMoveIdx + 1) % this.gasMoves.length;
        return move;
    }

    public getGasMove(index: number): MoveType {
        return this.gasMoves[index];
    }

    private getCharAt(c: Coord) {
        if (c.x < 0 || c.x >= this.width) {
            return this.Wall;
        }
        if (c.y < 0) {
            return this.Floor;
        }
        return this.grid.get(coordToStr(c)) || this.Air;
    }

    private moveRockIfPossible(rock: RockFigure, move: MoveType): boolean {
        // Returns true if the move had been possible;
        // False if the move was blocked by something else present on the board
        const newCoord = rock.getPositionAfterMove(move);
        const occupiedFields = rock.getOccupiedFields(newCoord);
        const firstIntersection = occupiedFields.find(c => this.getCharAt(c) !== this.Air);
        const isMovePossible = firstIntersection === undefined;

        if (isMovePossible) {
            rock.updatePosition(newCoord.x, newCoord.y);
        }
        return isMovePossible;
    }

    private saveFigure(rock: RockFigure): void {
        const occupiedFields = rock.getOccupiedFields();
        occupiedFields.forEach(c => this.grid.set(coordToStr(c), '#'));
    }

    private cutOffOldRows(): void {
        for (let y = this.height - 2 * this.cacheRowCount; y < this.height - this.maxHeight; y++) {
            for (let x = 0; x < this.width; x++) {
                this.grid.delete(coordToStr({ x: x, y: y }));
            }
        }
    }

    public dropNewRock(): void {
        const rock = this.getNextFigure();
        let isAtRest = false;
        while (!isAtRest) {
            // Push the rock with a hot gas
            const gasMove = this.getNextGasMove();
            this.moveRockIfPossible(rock, gasMove);
            // Push the rock down one unit if it is possible - otherwise the rock is at rest
            isAtRest = !this.moveRockIfPossible(rock, MoveDown);
        }
        this.saveFigure(rock);
        this.updateHeight();
        this.cutOffOldRows();
    }

    public seed(gasMoveIdx: number, figureIdx: number): void {
        this.gasMoveIdx = gasMoveIdx;
        this.rockFactory.seed(figureIdx);
    }

    public simulateWithCache(cycleIdx: number, gasMoveIdx: number, figureIdx: number): {
        cycleIdx: number, gasMoveIdx: number, figureIdx: number
    } {
        const cacheKey = this.getCacheKey(gasMoveIdx, figureIdx);
        const cache = this.cache.get(cacheKey);

        if (cache !== undefined) {
            const cycleLength = cache.rockCount;
            console.log(`Found a cycle of length ${cycleLength}: id = ${cycleIdx}, height = ${cache.height}`);
            console.log(`Skipping from ${cycleIdx} to ${cycleIdx + cycleLength}`);
            // TODO: Copy the previous rows to update the game state
            const updatedGrid: [string, string][] = Array.from(this.grid.keys()).map(k => {
                const coord = strToCoord(k);
                const newCoord = {
                    x: coord.x,
                    y: coord.y + cache.height - 1 // TODO: ??
                };
                return [coordToStr(newCoord), this.grid.get(k)];
            });
            this.grid = new Map<string, string>(updatedGrid);
            this.updateHeight();


            return {
                cycleIdx: cycleIdx + cache.rockCount + 1,
                gasMoveIdx: gasMoveIdx,
                figureIdx: figureIdx
            };
        } else {
            const rock = this.getFigure(figureIdx);
            let isAtRest = false;
            let gasIdx = gasMoveIdx;
            while (!isAtRest) {
                // Push the rock with a hot gas
                const gasMove = this.getGasMove(gasIdx);
                gasIdx = (gasIdx + 1) % this.gasMoves.length;

                this.moveRockIfPossible(rock, gasMove);
                // Push the rock down one unit if it is possible - otherwise the rock is at rest
                isAtRest = !this.moveRockIfPossible(rock, MoveDown);
            }
            this.saveFigure(rock);
            this.updateHeight();

            // Update cache
            this.cache.set(cacheKey, {
                height: this.getCurrentHeight(),
                rockCount: cycleIdx
            });

            this.cutOffOldRows();

            // Next cycle details
            return {
                cycleIdx: cycleIdx + 1,
                gasMoveIdx: gasIdx,
                figureIdx: (figureIdx + 1) % this.rockFactory.figureCount
            };
        }
    }

    private getCacheKey(gasMoveIdx: number, figureIdx: number): string {
        const representation = this.getStringRepresentation();
        return JSON.stringify({
            gasMoveIdx: gasMoveIdx,
            figureIdx: figureIdx,
            representation: representation
        });
    }

    private getStringRepresentation(): string {
        const rowRepresentations = [];
        for (let y = this.height - this.cacheRowCount; y < this.height; y++) {
            if (y >= 0) {
                let rowStr = '|';
                for (let x = 0; x < this.width; x++) {
                    const c = { x: x, y: y };
                    rowStr += this.getCharAt(c);
                }
                rowStr += '|';
                rowRepresentations.push(rowStr);
            }
        }
        return rowRepresentations.reverse().join('\n');
    }

    public print(withMovingRock?: RockFigure): void {
        const rowRepresentations = [];
        const rockFields = withMovingRock?.getOccupiedFields().map(c => coordToStr(c));
        for (let y = -1; y <= this.height + 4; y++) {
            let rowStr = '|';
            for (let x = 0; x < this.width; x++) {
                const c = { x: x, y: y };
                if (rockFields?.includes(coordToStr(c))) {
                    rowStr += '@';
                } else {
                    rowStr += this.getCharAt(c);
                }
            }
            rowStr += '|';
            rowRepresentations.push(rowStr);
        }
        console.log(rowRepresentations.reverse().join('\n'));
        console.log();
    }
}
