import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    private getElfSnacks(input: string): number[] {
        const elfs = input.split('\n\n');
        return elfs
            .map(e => e.split('\n')
                .reduce((sum, current) => sum + parseInt(current), 0));
    }


    public solveFirst(input: string): string {
        const result = Math.max(...this.getElfSnacks(input));
        return result.toString();
    }

    public solveSecond(input: string): string {
        const topElfSnacks = this.sortDesc(this.getElfSnacks(input)).slice(0, 3);
        return this.sum(topElfSnacks).toString();
    }

    public getFirstExpectedResult(): string {
        return '24000';
    }

    public getSecondExpectedResult(): string {
        return '45000';
    }
}
