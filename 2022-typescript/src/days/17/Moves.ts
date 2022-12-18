import { Coord } from '../../utils/mazes';

export type MoveType = string;

export const MoveLeft: MoveType = '<';

export const MoveRight: MoveType = '>';

export const MoveDown: MoveType = 'v';

export type MoveDir = (c: Coord) => Coord

export const Moves = new Map<MoveType, MoveDir>([
    [
        MoveLeft,
        (c: Coord) => {
            return { x: c.x - 1, y: c.y } as Coord;
        }],
    [
        MoveRight,
        (c: Coord) => {
            return { x: c.x + 1, y: c.y } as Coord;
        }],
    [
        MoveDown,
        (c: Coord) => {
            return { x: c.x, y: c.y - 1 } as Coord;
        }]
]);
