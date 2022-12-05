import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    private parseCrateArrangement(arrangement: string): Map<number, string[]> {
        const arrangements = arrangement.split('\n');
        const numCrates = parseInt(arrangements.pop().split('   ').pop());
        const crates: Map<number, string[]> = new Map<number, string[]>();

        arrangements.reverse().map(row => {
            Array.from(Array(numCrates)).forEach((_, index) => {
                const crate = row.substring(4 * index, 4 * index + 4);
                if (crate.trim()) {
                    const crateLetter = crate[1];
                    // Default map value = []
                    const updatedCrate: string[] = (crates.get(index + 1) ?? []).concat([crateLetter]);
                    crates.set(index + 1, updatedCrate);
                }
            });
        });

        return crates;
    }

    public solveFirst(): string {
        const [arrangement, procedures] = this.input.split('\n\n');
        const crates = this.parseCrateArrangement(arrangement);
        const instructionRegex = /move (\d+) from (\d+) to (\d+)/g;

        procedures.split('\n').map(instruction => {
            const matches = Array.from(instruction.matchAll(instructionRegex));
            matches.forEach(m => {
                const howMany = parseInt(m[1]);
                const from = parseInt(m[2]);
                const to = parseInt(m[3]);

                Array.from(Array(howMany)).forEach(_ => {
                    const toBeMoved = crates.get(from).pop();
                    crates.set(to, crates.get(to).concat([toBeMoved]));
                });
            });
        });

        const topOfStack = Array.from(crates.values()).map(crate => crate.slice(-1).pop());

        return topOfStack.join('');
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return 'TDCHVHJTG';
    }

    public solveSecond(): string {
        const [arrangement, procedures] = this.input.split('\n\n');
        const crates = this.parseCrateArrangement(arrangement);
        const instructionRegex = /move (\d+) from (\d+) to (\d+)/g;

        procedures.split('\n').map(instruction => {
            const matches = Array.from(instruction.matchAll(instructionRegex));
            matches.forEach(m => {
                const howMany = parseInt(m[1]);
                const from = parseInt(m[2]);
                const to = parseInt(m[3]);

                const toBeMoved = crates.get(from).splice(-howMany);
                crates.set(to, crates.get(to).concat(toBeMoved));
            });
        });

        const topOfStack = Array.from(crates.values()).map(crate => crate.slice(-1).pop());
        return topOfStack.join('');
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return 'NGCMPJLHV';
    }
}
