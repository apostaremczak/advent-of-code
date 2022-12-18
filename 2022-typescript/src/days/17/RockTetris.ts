import { Coord, coordToStr, strToCoord } from '../../utils/mazes';
import { RockFigure, RockFigureFactory } from './RockFigure';
import { MoveDown, MoveType } from './Moves';


export class RockTetris {
    private grid: Map<string, string>;
    private readonly Floor = '_';
    private readonly Wall = '|';
    private readonly Air = '.';
    private height: number;
    private rockFactory: RockFigureFactory;
    private gasMoveIdx: number;

    constructor(readonly gasMoves: string[], readonly width: number = 7, readonly maxHeight: number = 20) {
        this.grid = new Map<string, string>();
        this.height = 0;
        this.rockFactory = new RockFigureFactory();
        this.gasMoveIdx = 0;
    }

    private getNewFigure(): RockFigure {
        // Generate a new figure - two units from the left, three units above the other figures present
        return this.rockFactory.getNextRock(2, this.height + 3);
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
        for (let y = this.height - 2 * this.maxHeight; y < this.height - this.maxHeight; y++) {
            for (let x = 0; x < this.width; x++) {
                this.grid.delete(coordToStr({ x: x, y: y }));
            }
        }
    }

    public dropNewRock(): void {
        const rock = this.getNewFigure();
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
