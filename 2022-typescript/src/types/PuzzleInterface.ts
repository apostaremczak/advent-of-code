export interface PuzzleInterface {
  solveFirst: (input: string) => string;
  solveSecond: (input: string) => string;
  getFirstExpectedResult: () => string;
  getSecondExpectedResult: () => string;
  getInput: () => string;
  getTestInput: () => string;
}
