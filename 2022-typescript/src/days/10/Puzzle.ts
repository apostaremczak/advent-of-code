import Puzzle from '../../types/AbstractPuzzle';
import { rangeInclusive, sum } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    private resolveXValues(input: string): Map<number, number> {
        // key: cycle, value: X value at cycle from the key
        const XValues = new Map<number, number>([[0, 1]]);
        let X = 1;
        let currentCycle = 0;
        input.split('\n').forEach(instruction => {
            if (instruction === 'noop') {
                currentCycle += 1;
                XValues.set(currentCycle, X);
            } else {
                currentCycle += 1;
                XValues.set(currentCycle, X);
                currentCycle += 1;
                XValues.set(currentCycle, X);
                X += parseInt(instruction.split(' ')[1]);
            }
        });
        return XValues;
    }

    public solveFirst(input: string): string {
        const cyclesOfInterest = [20, 60, 100, 140, 180, 220];
        const XValues = this.resolveXValues(input);
        return sum(cyclesOfInterest.map(c => XValues.get(c) * c)).toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '13140';
    }

    public solveSecond(input: string): string {
        const XValues = this.resolveXValues(input);
        // console.log(XValues);
        let CRTRow = '';
        rangeInclusive(0, 239).forEach(CRT => {
            const currentPixel = CRT % 40;
            const X = XValues.get(CRT + 1);
            if (currentPixel=== 0) {
                CRTRow += '\n';
            }

            if (currentPixel >= X - 1 && currentPixel <= X + 1) {
                CRTRow += '#';
            } else {
                CRTRow += '.';
            }

        });
        return CRTRow;
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '\n##..##..##..##..##..##..##..##..##..##..\n' +
            '###...###...###...###...###...###...###.\n' +
            '####....####....####....####....####....\n' +
            '#####.....#####.....#####.....#####.....\n' +
            '######......######......######......####\n' +
            '#######.......#######.......#######.....';
    }
}
