export type Coord3D = {
  x: number,
  y: number,
  z: number
}

export function numsToStrCoords(x: number, y: number, z: number): string {
    return [x, y, z].join(',');
}

export function coordToStr(c: Coord3D): string {
    return numsToStrCoords(c.x, c.y, c.z);
}

export function strToCoord(s: string, sep=','): Coord3D {
    const [x, y, z] = s.split(sep);
    return {
        x: Number(x),
        y: Number(y),
        z: Number(z)
    };
}
