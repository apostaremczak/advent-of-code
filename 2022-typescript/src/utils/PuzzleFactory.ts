import Puzzle from '../types/AbstractPuzzle';
import readFile from './readFile';

class PuzzleFactory {
  public async getPuzzle(puzzleName: string) {
    const puzzlePath = `src/days/${puzzleName}`;
    let input = '';
    let testInput = '';
    try {
      input = await readFile(`${puzzlePath}/input.txt`);
      testInput = await readFile(`${puzzlePath}/test_input.txt`);
    } catch (error) {
      console.error(error);
      process.exit(1);
    }

    const puzzleModule: { default: { new (): Puzzle } } = await import(
      `../days/${puzzleName}/Puzzle`
    );

    const { default: puzzleClass } = puzzleModule;
    const puzzle = new puzzleClass();
    await puzzle.setInput(input);
    await puzzle.setTestInput(testInput);
    return puzzle;
  }
}

export default new PuzzleFactory();
