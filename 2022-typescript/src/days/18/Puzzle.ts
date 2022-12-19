import Puzzle from '../../types/AbstractPuzzle';
import { Coord3D, coordToStr, numsToStrCoords, strToCoord } from '../../utils/maze3d';
import { rangeInclusive, sum } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const coveredGrid = new Set<string>(input.split('\n'));

        const exposedSurface = sum(Array.from(coveredGrid.values())
            .map(coordStr => {
                const connectedSides = this.getConnectedSides(strToCoord(coordStr), coveredGrid);
                return 6 - connectedSides.length;
            }));

        return exposedSurface.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '64';
    }

    public solveSecond(input: string): string {
        const coveredGrid = new Set<string>(input.split('\n'));
        const allCoords = Array.from(coveredGrid.values()).map(s => strToCoord(s));
        const maxX = Math.max(...allCoords.map(c => c.x));
        const maxY = Math.max(...allCoords.map(c => c.y));
        const maxZ = Math.max(...allCoords.map(c => c.z));
        const spaceOutside = this.findSpaceOutsideDroplet(coveredGrid, maxX + 1, maxY + 1, maxZ + 1);

        const outsideSurface = sum(Array.from(coveredGrid.values())
            .map(coordStr => {
                return this.getConnectedSides(strToCoord(coordStr), spaceOutside).length;
            }));

        return outsideSurface.toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '58';
    }

    private DirVectors: number[][] = [-1, 1]
        .flatMap(diff => [[diff, 0, 0], [0, diff, 0], [0, 0, diff]]);

    private findSpaceOutsideDroplet(coveredGrid: Set<string>, maxX: number, maxY: number, maxZ: number): Set<string> {
        // Flood fill algorithm - find all the cubes within this range outside the droplet
        // https://en.wikipedia.org/wiki/Flood_fill

        const cubesOutside = new Set<string>();
        const cubesVisited = new Set<string>();
        // Start below all the other points to ensure we don't get trapped close to some 0-axis
        let queue = ['-1,-1,-1'];

        while (queue.length > 0) {
            const cube = queue.pop();
            cubesVisited.add(cube);

            if (!coveredGrid.has(cube)) {
                cubesOutside.add(cube);
                const coord = strToCoord(cube);
                const neighbors = this.DirVectors.map(([xOffset, yOffset, zOffset]) => {
                    const neighCoord = {
                        x: coord.x + xOffset,
                        y: coord.y + yOffset,
                        z: coord.z + zOffset
                    };
                    if (neighCoord.x > maxX || neighCoord.y > maxY || neighCoord.z > maxZ
                        || neighCoord.x < -1 || neighCoord.y < -1 || neighCoord.z < -1) {
                        return null;
                    }
                    return coordToStr(neighCoord);
                })
                    .filter(c => c !== null && !queue.includes(c) && !cubesVisited.has(c));

                queue = queue.concat(neighbors);

            }
        }

        return cubesOutside;
    }

    private getConnectedSides(coord: Coord3D, coveredGrid: Set<string>): Coord3D[] {
        // Returns the coordinates of other cubes connected to the currently chosen one
        const connected = [];
        for (const [xOffset, yOffset, zOffset] of this.DirVectors) {
            const sideCoord = {
                x: coord.x + xOffset,
                y: coord.y + yOffset,
                z: coord.z + zOffset
            };
            if (coveredGrid.has(coordToStr(sideCoord))) {
                connected.push(sideCoord);
            }
        }
        return connected;
    }
}
