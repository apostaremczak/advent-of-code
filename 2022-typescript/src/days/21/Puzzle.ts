import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const raw = new Map<string, string>(
            input.split('\n').map(line => {
                const [key, value] = line.split(': ');
                return [key, value];
            })
        );

        const evaluated = new Map<string, number>();

        for (const [key, rawValue] of raw.entries()) {
            const value = Number(rawValue);
            if (!isNaN(value)) {
                evaluated.set(key, value);
            }
        }

        const evaluate = (key: string) => {
            if (evaluated.has(key)) {
                return evaluated.get(key);
            }
            const [left, op, right] = raw.get(key).split(' ');
            const leftValue = evaluate(left);
            const rightValue = evaluate(right);
            let value: number;
            if (op === '+') {
                value = leftValue + rightValue;
            } else if (op === '-') {
                value = leftValue - rightValue;
            } else if (op === '*') {
                value = leftValue * rightValue;
            } else if (op === '/') {
                value = leftValue / rightValue;
            }
            evaluated.set(key, value);

            return value;
        };

        return evaluate('root').toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '152';
    }

    public solveSecond(input: string): string {
        const raw = new Map<string, string>(
            input.split('\n').map(line => {
                const [key, value] = line.split(': ');
                return [key, value];
            })
        );

        raw.delete('humn');

        const evaluated = new Map<string, number | string>();

        for (const [key, rawValue] of raw.entries()) {
            const value = Number(rawValue);
            if (!isNaN(value)) {
                evaluated.set(key, value);
            }
        }

        evaluated.set('humn', 'x');

        const evaluate: (key: string) => string | number = (key: string) => {
            if (evaluated.has(key)) {
                return evaluated.get(key);
            }
            const [left, op, right] = raw.get(key).split(' ');
            const leftValue = evaluate(left);
            const rightValue = evaluate(right);
            if (typeof leftValue === 'string' || typeof rightValue === 'string') {
                let substitutedValue: string;
                if (key === 'root') {
                    substitutedValue = `${leftValue}=${rightValue}`;
                } else {
                    substitutedValue = `(${leftValue}${op}${rightValue})`;
                }
                evaluated.set(key, substitutedValue);
                return substitutedValue;
            }

            let value: number;
            if (op === '+') {
                value = leftValue + rightValue;
            } else if (op === '-') {
                value = leftValue - rightValue;
            } else if (op === '*') {
                value = leftValue * rightValue;
            } else if (op === '/') {
                value = leftValue / rightValue;
            }
            evaluated.set(key, value);

            return value;
        };

        // Print the equation and put it into Wolfram Alpha
        return (evaluate('root') as string);
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '((4+(2*(x-3)))/4)=150';
    }
}
