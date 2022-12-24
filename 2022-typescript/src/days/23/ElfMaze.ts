import { Coord, coordToStr, numsToStrCoords, strToCoord } from "../../utils/mazes";

export default class ElfMaze {
    private elfPositions: Set<string>;
    private directions: ((c: Coord) => Coord)[];
    private directionChecks: ((c: Coord) => boolean)[];
    private allChecks: (c: Coord) => boolean;
    private allElfsAtRest = false;

  constructor(input: string) {
        const elfs: string[] = [];
        const lines = input.split('\n');
        for (const [y, row] of lines.entries()) {
            const rowChars = row.split('');
            for (const [x, char] of rowChars.entries()) {
                if (char === '#') {
                  elfs.push(numsToStrCoords(x, y));
                }
            }
        }
        this.elfPositions = new Set(elfs);

        const N = (pos: Coord) => ({ x: pos.x, y: pos.y - 1 });
        const NE = (pos: Coord) => ({ x: pos.x + 1, y: pos.y - 1 });
        const E = (pos: Coord) => ({ x: pos.x + 1, y: pos.y });
        const SE = (pos: Coord) => ({ x: pos.x + 1, y: pos.y + 1 });
        const S = (pos: Coord) => ({ x: pos.x, y: pos.y + 1 });
        const SW = (pos: Coord) => ({ x: pos.x - 1, y: pos.y + 1 });
        const W = (pos: Coord) => ({ x: pos.x - 1, y: pos.y });
        const NW = (pos: Coord) => ({ x: pos.x - 1, y: pos.y - 1 });

        // If there is no Elf in the N, NE, or NW adjacent positions, the Elf proposes moving north one step.
        const northCheck = (pos: Coord) => !this.isOccupied(N(pos)) && !this.isOccupied(NE(pos)) && !this.isOccupied(NW(pos));
        
        // If there is no Elf in the S, SE, or SW adjacent positions, the Elf proposes moving south one step.
        const southCheck = (pos: Coord) => !this.isOccupied(S(pos)) && !this.isOccupied(SE(pos)) && !this.isOccupied(SW(pos));
        
        // If there is no Elf in the W, NW, or SW adjacent positions, the Elf proposes moving west one step.
        const westCheck = (pos: Coord) => !this.isOccupied(W(pos)) && !this.isOccupied(NW(pos)) && !this.isOccupied(SW(pos));
        
        // If there is no Elf in the E, NE, or SE adjacent positions, the Elf proposes moving east one step.
        const eastCheck = (pos: Coord) => !this.isOccupied(E(pos)) && !this.isOccupied(NE(pos)) && !this.isOccupied(SE(pos));

        this.allChecks = (pos: Coord) => northCheck(pos) && southCheck(pos) && westCheck(pos) && eastCheck(pos);
        this.directionChecks = [northCheck, southCheck, westCheck, eastCheck];
        this.directions = [N, S, W, E];

    }

    private isOccupied(position: Coord): boolean {
        return this.elfPositions.has(coordToStr(position));
    }


    public move(): void {
        // Position, elfs trying to get there
        const proposedMoves = new Map<string, string[]>();
        for (const elfPosition of this.elfPositions) {
            const pos = strToCoord(elfPosition);

            // If there are no other elfs around you, stay in this place
            const checkResults: boolean[] = this.directionChecks.map(check => check(pos));
            if (checkResults.every(Boolean)) {
                continue;
            }
            // Look for possibilities to move
            const checkIndex = checkResults.indexOf(true);
            if (checkIndex !== -1) {
                const claim = coordToStr(this.directions[checkIndex](pos));
                proposedMoves.set(claim, (proposedMoves.get(claim) || []).concat([elfPosition]));
            }

            // Otherwise stay in this place - do not propose a new move
          };


          // All elfs are at rest if nobody needs to move anymore
          this.allElfsAtRest = proposedMoves.size === 0;

          for( const [desiredPosition, claimingElfs] of proposedMoves.entries()) {
            if (claimingElfs.length === 1) {
                // Move there if this elf was the only one who wanted to go there
                this.elfPositions.delete(claimingElfs[0]);
                this.elfPositions.add(desiredPosition);
            }
          }

          // Shift the directions and checks by one
          const firstCheck = this.directionChecks.shift()
          this.directionChecks.push(firstCheck);
          const firstDirection = this.directions.shift()
          this.directions.push(firstDirection);
    }

    private toString(): string {
        const lines = [];
        const borders = this.getBorders();
        for (let y = borders.yMin; y <= borders.yMax; y++) {
            const rowChars = [];
            for (let x = borders.xMin; x <= borders.xMax; x++) {
                if (this.elfPositions.has(numsToStrCoords(x, y))) {
                    rowChars.push('#');
                } else {
                    rowChars.push('.');
                }
            }
            lines.push(rowChars.join(''));
        }
        return lines.join('\n');
    }

    public print(): void {
        console.log(this.toString());
    }

    private getBorders(): {xMin: number, xMax: number, yMin: number, yMax: number} {
        const allCoords = Array.from(this.elfPositions).map(s => strToCoord(s));
        const xs = allCoords.map(c => c.x);
        const ys = allCoords.map(c => c.y);

        return {
            xMin: Math.min(...xs),
            xMax: Math.max(...xs),
            yMin: Math.min(...ys),
            yMax: Math.max(...ys)
        }
    }

    public countEmptyTiles(): number {
        const borders = this.getBorders();
        const area = (borders.xMax - borders.xMin + 1) * (borders.yMax - borders.yMin + 1);
        return area - this.elfPositions.size;
    }

    public findRestRoundNumber(): number {
        let i = 0;
        while (!this.allElfsAtRest) {
            i +=1;
            if (i % 50 === 0) {
                console.log(`Round ${i}`);
            }
            this.move();
        }

        return i;
    }
}
