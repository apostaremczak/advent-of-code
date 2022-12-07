import Puzzle from '../../types/AbstractPuzzle';
import File from './File';
import { sum } from '../../utils/arrays';
import { findAllIndices } from '../../utils/strings';

export default class ConcretePuzzle extends Puzzle {
    private FileRegex = /(\d+) (\S+)/g;
    private ListCommand = 'ls';

    public solveFirst(input: string): string {
        const maxDirSize = 100000;
        const sizes = this.getDirSizes(input);
        const smallEnough = Array.from(sizes.values()).filter(s => s <= maxDirSize);

        return sum(smallEnough).toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '95437';
    }

    public solveSecond(input: string): string {
        const availableMemory = 70000000;
        const minFreed = 30000000;
        const sizes = this.getDirSizes(input);
        const rootDirMem = sizes.get('/');
        const unused = availableMemory - rootDirMem;

        const potentialDeletions = Array.from(sizes.values())
            .filter(size => size + unused >= minFreed);

        return Math.min(...potentialDeletions).toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '24933642';
    }

    private getDirSizes(input: string): Map<string, number> {
        const files = this.getAllFiles(input);
        const sizes = new Map<string, number>();

        files.forEach(file => {
            // There might be some nested dirs that are not explicitly listed if they have no files inside,
            // only other directories - add them!
            // Example: /a/b/c/d/e/file.txt
            const allDirsAbove = this.getAllDirsAbove(file.absolutePath);
            allDirsAbove.forEach(dir => {
                sizes.set(dir, (sizes.get(dir) ?? 0) + file.size);
            });
        });

        return sizes;
    }

    private getAllDirsAbove(path: string): string[] {
        const locations = findAllIndices(path, '/');
        return locations.filter(i => i >= 0).map(i => path.slice(0, i + 1));
    }

    private getAllFiles(input: string): File[] {
        const commands = input.split('\n$ ').slice(1);
        const files: File[] = [];

        let currentPath = '/';
        commands.forEach(outputLines => {
            if (outputLines.startsWith(this.ListCommand)) {
                const matches = Array.from(outputLines.matchAll(this.FileRegex));
                matches.forEach(m => {
                    const fileSize = parseInt(m[1]);
                    const fileName = m[2];
                    const file = new File(currentPath, fileName, fileSize);
                    files.push(file);
                });
            } else if (outputLines.startsWith('cd')) {
                // Changing the directory
                const dirName = outputLines.trim().split(' ')[1];
                currentPath = this.resolveNewPath(currentPath, dirName);
            } else {
                throw new Error(`Unknown command! ${outputLines}`);
            }
        });
        return files;
    }

    private getDirAbove(path: string): string {
        return path.slice(0, path.lastIndexOf('/'));
    }

    private resolveNewPath(currentPath: string, newDirName: string): string {
        if (newDirName === '/') {
            return newDirName;
        }
        if (newDirName === '..') {
            return this.getDirAbove(currentPath);
        }
        if (currentPath === '/') {
            return `/${newDirName}`;
        }
        return `${currentPath}/${newDirName}`;
    }
}
