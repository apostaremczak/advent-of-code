import { Coord } from '../../utils/mazes';
import { MoveType, Moves } from './Moves';

// The numbers in each figure represent the positions in a 4x4 matrix where the figure is solid
const RockMinus = {
    fields: [12, 13, 14, 15],
    width: 4,
    char: '-'
};

const RockPlus = {
    fields: [5, 8, 9, 10, 13],
    width: 3,
    char: '+'
};

const RockL = {
    fields: [6, 10, 12, 13, 14],
    width: 3,
    char: 'L'
};

const RockI = {
    fields: [0, 4, 8, 12],
    width: 1,
    char: 'I'
};

const RockSquare = {
    fields: [8, 9, 12, 13],
    width: 2,
    char: 'o'
};

const Figures = [
    RockMinus,
    RockPlus,
    RockL,
    RockI,
    RockSquare
];

export class RockFigure {
    public x: number;
    public y: number;
    public char: string;

    constructor(x: number, y: number, readonly typeNum: number) {
        this.x = x;
        this.y = y;
        this.char = Figures[this.typeNum].char;
    }

    private getImage(): number[] {
        return Figures[this.typeNum].fields;
    }

    public updatePosition(newX: number, newY: number): void {
        this.x = newX;
        this.y = newY;
    }

    public getPositionAfterMove(moveType: MoveType): Coord {
        const dir = Moves.get(moveType);
        return dir({ x: this.x, y: this.y });
    }

    public getOccupiedFields(relativeTo: Coord = { x: this.x, y: this.y }): Coord[] {
        const occupied: Coord[] = [];
        for (const offset of this.getImage()) {
            const xOffset = offset % 4;
            const yOffset = 3 - Math.floor(offset / 4);
            occupied.push({ x: relativeTo.x + xOffset, y: relativeTo.y + yOffset });
        }
        return occupied;
    }
}

export class RockFigureFactory {
    private rockIdx = 0;
    private figureCount = Figures.length;

    public getNextRock(x: number, y: number): RockFigure {
        const rock = new RockFigure(x, y, this.rockIdx);
        this.rockIdx = (this.rockIdx + 1) % this.figureCount;
        return rock;
    }
}
