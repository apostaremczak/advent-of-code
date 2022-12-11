import Puzzle from '../../types/AbstractPuzzle';
import Monkey from './Monkey';
import { sortDesc } from '../../utils/arrays';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const monkeys = new Map<number, Monkey>();
        input.split('\n\n').forEach(instruction => {
            const monkey = new Monkey(instruction);
            monkeys.set(monkey.idx, monkey);
        });
        Array.from(Array(20)).forEach(_ => {
            this.playRound(monkeys);
        });

        const itemsInspected = sortDesc(Array.from(monkeys.values()).map(m => m.inspectedItemsCount));
        const monkeyBusiness = itemsInspected[0] * itemsInspected[1];
        return monkeyBusiness.toString();
    }

    private playRound(monkeys: Map<number, Monkey>, lcm?: number): void {
        Array.from(monkeys.keys()).forEach(idx => {
            const currentMonkey = monkeys.get(idx);
            currentMonkey.itemsForInspection.forEach(item => {
                    const worryLevel = currentMonkey.inspect(item, lcm);
                    const nextMonkeyIdx = (currentMonkey.test(worryLevel)) ? currentMonkey.throwOnTrue : currentMonkey.throwOnFalse;
                    monkeys.get(nextMonkeyIdx).addForInspection(worryLevel);
                }
            );
        });
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '10605';
    }

    public solveSecond(input: string): string {
        const monkeys = new Map<number, Monkey>();
        input.split('\n\n').forEach(instruction => {
            const monkey = new Monkey(instruction);
            monkeys.set(monkey.idx, monkey);
        });
        const lcm = Array.from(monkeys.values())
            .map(m => m.divisibleBy)
            .reduce((x, current) => x * current, 1);

        Array.from(Array(10000)).forEach(_ => {
            this.playRound(monkeys, lcm);
        });

        const itemsInspected = sortDesc(Array.from(monkeys.values()).map(m => m.inspectedItemsCount));
        const monkeyBusiness = itemsInspected[0] * itemsInspected[1];
        return monkeyBusiness.toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '2713310158';
    }
}
