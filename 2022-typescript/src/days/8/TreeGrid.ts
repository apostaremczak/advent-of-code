export default class TreeGrid {
    public readonly maxRow: number;
    public readonly maxCol: number;

    constructor(
        // row,col -> height
        readonly grid: Map<string, number>
    ) {
        const coords = Array.from(grid.keys());
        this.maxRow = Math.max(...coords.map(c => parseInt(c.split(',')[0])));
        this.maxCol = Math.max(...coords.map(c => parseInt(c.split(',')[1])));
    }

    private get(row: number, col: number): number {
        return this.grid.get([row, col].join(','));
    }

    public isTreeVisible(row: number, col: number): boolean {
        const treeHeight = this.get(row, col);

        let visibleFromTop = true;
        let currentRow = 0;
        let currentColumn = col;
        while (visibleFromTop && currentRow < row) {
            if (this.get(currentRow, currentColumn) >= treeHeight) {
                visibleFromTop = false;
            }
            currentRow += 1;
        }

        if (visibleFromTop) {
            return true;
        }

        let visibleFromBottom = true;
        currentRow = row + 1;
        while (visibleFromBottom && currentRow <= this.maxRow) {
            if (this.get(currentRow, currentColumn) >= treeHeight) {
                visibleFromBottom = false;
            }
            currentRow += 1;
        }
        if (visibleFromBottom) {
            return true;
        }

        let visibleFromLeft = true;
        currentRow = row;
        currentColumn = 0;
        while (visibleFromLeft && currentColumn < col) {
            if (this.get(currentRow, currentColumn) >= treeHeight) {
                visibleFromLeft = false;
            }
            currentColumn += 1;
        }
        if (visibleFromLeft) {
            return true;
        }

        let visibleFromRight = true;
        currentColumn = col + 1;
        while (visibleFromRight && currentColumn <= this.maxCol) {
            if (this.get(currentRow, currentColumn) >= treeHeight) {
                visibleFromRight = false;
            }
            currentColumn += 1;
        }

        return visibleFromRight;
    }

    public getScenicScore(row: number, col: number): number {
        if (row === 0 || col === 0 || row === this.maxRow || col === this.maxCol) {
            return 0;
        }
        const treeHeight = this.get(row, col);
        let visibleFromTop = 0;
        let currentRow = row - 1;
        let currentColumn = col;
        let isBlocked = false;
        while (!isBlocked && currentRow >= 0) {
            const currentTreeHeight = this.get(currentRow, currentColumn);
            if (currentTreeHeight <= treeHeight) {
                visibleFromTop += 1;
                if (currentTreeHeight === treeHeight) {
                    isBlocked = true;
                }
            } else {
                isBlocked = true;
            }
            currentRow -= 1;
        }
        if (visibleFromTop === 0) {
            return 0;
        }

        let visibleFromBottom = 0;
        currentRow = row + 1;
        isBlocked = false;
        while (!isBlocked && currentRow <= this.maxRow) {
            const currentTreeHeight = this.get(currentRow, currentColumn);
            if (currentTreeHeight <= treeHeight) {
                visibleFromBottom += 1;
                if (currentTreeHeight === treeHeight) {
                    isBlocked = true;
                }
            } else {
                isBlocked = true;

            }
            currentRow += 1;
        }
        if (visibleFromBottom === 0) {
            return 0;
        }

        let visibleFromLeft = 0;
        currentRow = row;
        currentColumn = col - 1;
        isBlocked = false;
        while (!isBlocked && currentColumn >= 0) {
            const currentTreeHeight = this.get(currentRow, currentColumn);
            if (currentTreeHeight <= treeHeight) {
                visibleFromLeft += 1;
                if (currentTreeHeight === treeHeight) {
                    isBlocked = true;
                }
            } else {
                isBlocked = true;

            }
            currentColumn -= 1;
        }
        if (visibleFromLeft === 0) {
            return 0;
        }

        let visibleFromRight = 0;
        currentColumn = col + 1;
        isBlocked = false;
        while (!isBlocked && currentColumn <= this.maxCol) {
            const currentTreeHeight = this.get(currentRow, currentColumn);
            if (currentTreeHeight <= treeHeight) {
                visibleFromRight += 1;
                if (currentTreeHeight === treeHeight) {
                    isBlocked = true;
                }
            } else {
                isBlocked = true;
                visibleFromRight += 1;

            }
            currentColumn += 1;
        }

        return visibleFromTop * visibleFromBottom * visibleFromLeft * visibleFromRight;
    }
}
