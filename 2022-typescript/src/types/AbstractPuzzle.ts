import { PuzzleInterface } from './PuzzleInterface';

export default abstract class Puzzle implements PuzzleInterface {
    protected input: string;
    protected testInput: string;

    public async setInput(input: string) {
        this.input = input;
    }

    public async setTestInput(input: string) {
        this.testInput = input;
    }

    protected sortDesc(nums: number[]): number[] {
        return nums.sort((one, two) => (one > two ? -1 : 1));
    }

    protected sum(nums: number[]): number {
        return nums.reduce((sum, current) => sum + current, 0);
    }

    public abstract solveFirst(): string;

    public abstract getFirstExpectedResult(): string;

    public abstract solveSecond(): string;

    public abstract getSecondExpectedResult(): string;
}
