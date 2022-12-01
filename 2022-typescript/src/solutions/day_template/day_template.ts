import { readFileSync } from 'fs';
import { join } from 'path';

// ✅ read file SYNCHRONOUSLY
function readFile(filename: string): string {
    const result = readFileSync(join(__dirname, filename), 'utf-8');
    return result;
}

function readSplitByLines(filename: string): string[] {
    return readFile(filename).split("\n");
}

function readNumsSplitByLines(filename: string): number[] {
    return readSplitByLines(filename).map(parseInt)
}

function sortDesc(nums: number[]): number[] {
    return nums.sort((one, two) => (one > two ? -1 : 1));
}

function sum(nums: number[]): number {
    return nums.reduce((sum, current) => sum + current, 0)
}

