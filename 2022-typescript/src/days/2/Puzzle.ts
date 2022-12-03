import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    private RockScore = 1;
    private PaperScore = 2;
    private ScissorsScore = 3;
    private DrawScore = 3;
    private WinScore = 6;
    private TheirRock = 'A';
    private TheirPaper = 'B';
    private TheirScissors = 'C';


    public solveFirst(): string {
        const MyRock = 'X';
        const MyPaper = 'Y';

        const games: string[] = this.input.split('\n');
        const results: number[] = games.map(game => {
            const choices = game.split(' ');
            const theirs = choices[0];
            const mine = choices[1];
            let result = 0;

            if (mine === MyRock) {
                result = this.RockScore;
                // I only win if they chose scissors
                if (theirs === this.TheirScissors) {
                    result += this.WinScore;
                } else if (theirs == this.TheirRock) {
                    result += this.DrawScore;
                }
            } else if (mine === MyPaper) {
                result = this.PaperScore;
                // I only win if they chose rock
                if (theirs === this.TheirRock) {
                    result += this.WinScore;
                } else if (theirs == this.TheirPaper) {
                    result += this.DrawScore;
                }
            } else {
                result = this.ScissorsScore;
                // I chose scissors. I only win if they chose paper
                if (theirs === this.TheirPaper) {
                    result += this.WinScore;
                } else if (theirs === this.TheirScissors) {
                    result += this.DrawScore;
                }
            }

            return result;
        });

        const gameSum = results.reduce((s, c) => s + c, 0);
        return gameSum.toString();
    }

    public getFirstExpectedResult(): string {
        return '15';
    }

    public solveSecond(): string {
        const Win = 'Z';
        const Draw = 'Y';

        const games: string[] = this.input.split('\n');
        const results: number[] = games.map(game => {
            const choices = game.split(' ');
            const theirs = choices[0];
            const mine = choices[1];
            let result = 0;

            if (mine === Win) {
                result += this.WinScore;
                if (theirs === this.TheirRock) {
                    // I need to choose paper
                    result += this.PaperScore;
                } else if (theirs === this.TheirPaper) {
                    // I need to choose scissors
                    result += this.ScissorsScore;
                } else {
                    // They chose scissors. I need to choose rock.
                    result += this.RockScore;
                }
            } else if (mine === Draw) {
                result += this.DrawScore;
                if (theirs === this.TheirRock) {
                    result += this.RockScore;
                } else if (theirs === this.TheirPaper) {
                    result += this.PaperScore;
                } else {
                    result += this.ScissorsScore;
                }
            } else {
                // I need to loose
                if (theirs === this.TheirRock) {
                    // I need to choose scissors
                    result += this.ScissorsScore;
                } else if (theirs === this.TheirPaper) {
                    // I need to choose rock
                    result += this.RockScore;
                } else {
                    // They chose scissors. I need to choose paper.
                    result += this.PaperScore;
                }
            }

            return result;
        });

        const gameSum = results.reduce((s, c) => s + c, 0);
        return gameSum.toString();
    }

    public getSecondExpectedResult(): string {
        return '12';
    }
}
