import PuzzleFactory from './utils/PuzzleFactory';

const args = process.argv.slice(2);
const dayToSolve = args[0];

if (!dayToSolve) {
    console.error('No day specified run with npm run dev {day}');
    process.exit(1);
}
console.log(`Solving Day #${args[0]}`);
(async () => {
    const puzzle = await PuzzleFactory.getPuzzle(args[0]);

    const firstTestResult = puzzle.solveFirst(puzzle.getTestInput());
    const firstExpectedResult = puzzle.getFirstExpectedResult();
    console.log(`[TEST] Part 1: ${firstTestResult === firstExpectedResult ? ('✅ ') : (`❌  expected ${firstExpectedResult}, got ${firstTestResult}`)}`);
    console.log('Part 1: ', puzzle.solveFirst(puzzle.getInput()));
    const secondTestResult = puzzle.solveSecond(puzzle.getTestInput());
    const secondExpectedResult = puzzle.getSecondExpectedResult();
    console.log(`[TEST] Part 2: ${secondTestResult === secondExpectedResult ? ('✅ ') : (`❌  expected ${secondExpectedResult}, got ${secondTestResult}`)}`);
    console.log('Part 2: ', puzzle.solveSecond(puzzle.getInput()));
})();
