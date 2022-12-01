import { readFileSync } from 'fs';
import { join } from 'path';

function readFile(filename: string): string {
    const result = readFileSync(join(__dirname, filename), 'utf-8');
    return result;
}

function readSplitByElf(filename: string): string[] {
    return readFile(filename).split("\n\n");
}

function sortDesc(nums: number[]): number[] {
    return nums.sort((one, two) => (one > two ? -1 : 1));
}

function sum(nums: number[]): number {
    return nums.reduce((sum, current) => sum + current, 0)
}

let elfs = readSplitByElf("01.txt");

let elfSnacks = elfs.map(e => e.split("\n").reduce((sum, current) => sum + parseInt(current), 0));
console.log(Math.max(...elfSnacks));

let topElfSnacks = sortDesc(elfSnacks).slice(0, 3)
console.log(sum(topElfSnacks));
