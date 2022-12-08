import Puzzle from '../../types/AbstractPuzzle';
import TreeGrid from './TreeGrid';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const treeGrid = this.parseGrid(input);

        const visibleTrees = Array.from(treeGrid.grid.keys())
            .filter(rowCol => {
                const [row, col] = rowCol.split(',');
                return treeGrid.isTreeVisible(parseInt(row), parseInt(col));
            });

        return visibleTrees.length.toString();
    }

    public getFirstExpectedResult(): string {
        return '21';
    }

    public solveSecond(input: string): string {
        const treeGrid = this.parseGrid(input);

        const scenicScores = Array.from(treeGrid.grid.keys())
            .map(rowCol => {
                const [row, col] = rowCol.split(',');
                return treeGrid.getScenicScore(parseInt(row), parseInt(col));
            });

        return Math.max(...scenicScores).toString();
    }

    public getSecondExpectedResult(): string {
        return '8';
    }

    private parseGrid(input: string): TreeGrid {
        const grid = new Map<string, number>();
        input
            .split('\n')
            .forEach((row, row_idx) => {
                row
                    .split('')
                    .forEach((height, col_idx) => {
                        grid.set([row_idx, col_idx].join(','), parseInt(height));
                    });
            });

        return new TreeGrid(grid);
    }
}
