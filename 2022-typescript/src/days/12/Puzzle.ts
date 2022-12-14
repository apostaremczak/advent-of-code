import Puzzle from '../../types/AbstractPuzzle';
import { numsToCoords } from '../../utils/mazes';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const m = new Heightmap(input);
        const path = m.findShortestPathLengthFromStartToEnd();
        return path.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '31';
    }

    public solveSecond(input: string): string {
        const m = new Heightmap(input);
        const lowestPoints = m.getLowestPoints();
        const distances = lowestPoints
            .map(start => m.findShortestPathLengthFromStartToEnd(start))
            .filter(d => d > 0);
        return Math.min(...distances).toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '29';
    }
}

class HeightNode {
    private neighbors: HeightNode[] = [];

    constructor(readonly height: string, readonly x: number, readonly y: number) {
    }

    public addNeighbor(neighbor: HeightNode): void {
        this.neighbors.push(neighbor);
    }

    public getNeighbors(): HeightNode[] {
        return this.neighbors;
    }


    public equals(other: HeightNode): boolean {
        return this.x === other.x && this.y === other.y;
    }

    private areAlphabeticallyAdjacent(char1: string, char2: string): boolean {
        const code1 = char1.toLowerCase().charCodeAt(0);
        const code2 = char2.toLowerCase().charCodeAt(0);
        return Math.abs(code1 - code2) === 1;
    }

    public getCoords(): string {
        return numsToCoords(this.x, this.y);
    }

    public isMovePossible(to: HeightNode): boolean {
        if (this.height === 'S') {
            return true;
        }
        if (to.height === 'E') {
            return this.height === 'z' || this.height === 'y';
        }
        if (this.height >= to.height) {
            return true;
        }
        return this.areAlphabeticallyAdjacent(this.height, to.height);
    }

}

class Heightmap {
    private start: HeightNode;
    private end: HeightNode;
    private nodes: Map<string, HeightNode>;

    constructor(input: string) {
        const rows = input.split('\n');
        const nodesArray: HeightNode[][] = rows
            .map((row, x) =>
                row.split('')
                    .map((heightSymbol, y) => {
                        const node = new HeightNode(heightSymbol, x, y);
                        if (heightSymbol === 'S') {
                            this.start = node;
                        }
                        if (heightSymbol === 'E') {
                            this.end = node;
                        }
                        return node;
                    })
            );

        const nodes = new Map<string, HeightNode>();
        nodesArray.forEach((row, x) => {
            row.forEach((node, y) => {
                const coord = numsToCoords(x, y);
                if (x >= 1) {
                    node.addNeighbor(nodesArray[x - 1][y]);
                }
                if (x < nodesArray.length - 1) {
                    node.addNeighbor(nodesArray[x + 1][y]);
                }
                if (y >= 1) {
                    node.addNeighbor(nodesArray[x][y - 1]);
                }
                if (y < row.length - 1) {
                    node.addNeighbor(nodesArray[x][y + 1]);
                }
                nodes.set(coord, node);
            });
        });
        this.nodes = nodes;
    }

    public findShortestPathLengthFromStartToEnd(startNode: HeightNode = this.start): number {
        const visited = new Set<string>([startNode.getCoords()]);
        const queue = [{ vertex: startNode, dist: 0 }];
        while (queue.length > 0) {
            const { vertex, dist } = queue.shift();
            if (vertex.equals(this.end)) {
                return dist;
            }
            for (const n of vertex.getNeighbors()) {
                if (!visited.has(n.getCoords()) && vertex.isMovePossible(n)) {
                    queue.push({ vertex: n, dist: dist + 1 });
                    visited.add(n.getCoords());
                }
            }
        }
        // No path found
        return -1;
    }

    public getLowestPoints(): HeightNode[] {
        return Array.from(this.nodes.values())
            .filter(node => node.height === 'a' || node.height === 'S');
    }
}
