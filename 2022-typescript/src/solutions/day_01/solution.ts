import { join } from 'path';
import { readSplitByDoubleEmpty } from '../../utils/readFile'
import { sum, sortDesc } from '../../utils/arrays'


let elfs: string[] = readSplitByDoubleEmpty(join(__dirname, "01.txt"));

let elfSnacks = elfs.map(e => e.split("\n").reduce((s, c) => s + parseInt(c), 0));
console.log(Math.max(...elfSnacks));

let topElfSnacks = sortDesc(elfSnacks).slice(0, 3)
console.log(sum(topElfSnacks));
