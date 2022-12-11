const InstructionRegex = /Monkey (\d+):\n\s+Starting items: (.+)\n\s+Operation: new = (.+)\n\s+Test: divisible by (\d+)\n\s+If true: throw to monkey (\d+)\n\s+If false: throw to monkey (\d+)/g;
const Operators = new Map([
    ['+', (a: number, b: number) => a + b],
    ['*', (a: number, b: number) => a * b],
]);

export default class Monkey {
    public idx: number;
    public itemsForInspection: number[];
    public operation: (old: number) => number;
    public test: (x: number) => boolean;
    public throwOnTrue: number;
    public throwOnFalse: number;
    public inspectedItemsCount = 0;
    public divisibleBy: number;

    constructor(instruction: string) {
        const matches = Array.from(instruction.matchAll(InstructionRegex))[0];

        this.idx = Number(matches[1]);

        this.itemsForInspection = matches[2].split(', ').map(d => Number(d));

        const [before, operator, after] = matches[3].split(' ');
        this.operation = (old: number) => {
            if (before === 'old') {
                if (after === 'old') {
                    return Operators.get(operator)(old, old);
                } else {
                    return Operators.get(operator)(old, Number(after));
                }
            } else {
                throw new Error(`Started from an unknown argument: ${before}`);
            }
        };

        this.divisibleBy = Number(matches[4]);
        this.test = (x: number) => x % this.divisibleBy === 0;

        this.throwOnTrue = Number(matches[5]);
        this.throwOnFalse = Number(matches[6]);
    }

    public inspect(itemLevel: number, lcm?: number): number {
        this.markItemAsInspected();
        if (lcm !== undefined) {
            return this.operation(itemLevel) % lcm;
        } else {
            return Math.floor(this.operation(itemLevel) / 3);
        }
    }

    private markItemAsInspected() {
        this.inspectedItemsCount += 1;
        this.itemsForInspection = this.itemsForInspection.slice(1, this.itemsForInspection.length);
    }

    public addForInspection(itemLevel: number): void {
        this.itemsForInspection.push(itemLevel);
    }
}
