import { readFileSync } from 'fs';
import { join } from 'path';

// ✅ read file SYNCHRONOUSLY
export function readFile(filename: string): string {
    const result = readFileSync(join(__dirname, filename), 'utf-8');
    return result;
}

export function readSplitByLines(filename: string): string[] {
    return readFile(filename).split("\n");
}

export function readNumsSplitByLines(filename: string): number[] {
    return readSplitByLines(filename).map(parseInt)
}

export function readSplitByDoubleEmpty(filename: string): string[] {
    return readFile(filename).split("\n");
}
