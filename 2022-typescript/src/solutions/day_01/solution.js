"use strict";
exports.__esModule = true;
var fs_1 = require("fs");
var path_1 = require("path");
// ✅ read file SYNCHRONOUSLY
function readFile(filename) {
    var result = (0, fs_1.readFileSync)((0, path_1.join)(__dirname, filename), 'utf-8');
    return result;
}
function readSplitByElf(filename) {
    return readFile(filename).split("\n\n");
}
// function readNumsSplitByLines(filename: string): number[] {
//     return readSplitByLines(filename).map(parseInt);
// }
function sortDesc(nums) {
    return nums.sort(function (one, two) { return (one > two ? -1 : 1); });
}
function sum(nums) {
    return nums.reduce(function (sum, current) { return sum + current; }, 0);
}
var elfs = readSplitByElf("01.txt");
var elfSnacks = elfs.map(function (e) { return e.split("\n").reduce(function (sum, current) { return sum + parseInt(current); }, 0); });
console.log(Math.max.apply(Math, elfSnacks));
var topElfSnacks = sortDesc(elfSnacks).slice(0, 3);
console.log(sum(topElfSnacks));
