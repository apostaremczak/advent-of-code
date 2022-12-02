import {readSplitByLines} from '../../utils/readFiles';
import {join} from "path";

let games: string[] = readSplitByLines(join(__dirname, "02.txt"));
console.log(games);
