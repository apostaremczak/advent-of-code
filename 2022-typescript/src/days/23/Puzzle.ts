import Puzzle from '../../types/AbstractPuzzle';
import { numsToStrCoords, strToCoord } from '../../utils/mazes';
import ElfMaze from './ElfMaze';

export default class ConcretePuzzle extends Puzzle {
  public solveFirst(input: string): string {
    const elfMaze = new ElfMaze(input);

    for (let i = 1; i <= 10; i++) {
      elfMaze.move();
    }

    return elfMaze.countEmptyTiles().toString();
  }

  public getFirstExpectedResult(): string {
    // RETURN EXPECTED SOLUTION FOR TEST 1;
    return '110';
  }

  public solveSecond(input: string): string {
    const elfMaze = new ElfMaze(input);
    return elfMaze.findRestRoundNumber().toString();
  }

  public getSecondExpectedResult(): string {
    // RETURN EXPECTED SOLUTION FOR TEST 2;
    return '20';
  }
}
