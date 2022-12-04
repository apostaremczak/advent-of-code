import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    private getElfSnacks(): number[] {
        const elfs = this.input.split('\n\n');
        const snacks = elfs
            .map(e => e.split('\n')
                .reduce((sum, current) => sum + parseInt(current), 0));
        return snacks;
    }


    solveFirst(): string {
        const result = Math.max(...this.getElfSnacks());
        return result.toString();
    }

    public solveSecond(): string {
        const topElfSnacks = this.sortDesc(this.getElfSnacks()).slice(0, 3);
        return this.sum(topElfSnacks).toString();
    }

    public getFirstExpectedResult(): string {
        return '70698';
    }

    public getSecondExpectedResult(): string {
        return '206643';
    }
}
