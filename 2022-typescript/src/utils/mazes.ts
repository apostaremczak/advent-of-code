export type Coord = { x: number, y: number };

export function numsToStrCoords(x: number, y: number): string {
    return [x, y].join(',');
}

export function coordToStr(c: Coord): string {
    return numsToStrCoords(c.x, c.y);
}

export function getManhattanDistance(a: Coord, b: Coord): number {
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
}

export function equal(a: Coord, b: Coord): boolean {
    return a.x === b.x && a.y === b.y;
}

export function strToCoord(s: string): Coord {
    const [x, y] = s.split(',');
    return { x: Number(x), y: Number(y) };
}
