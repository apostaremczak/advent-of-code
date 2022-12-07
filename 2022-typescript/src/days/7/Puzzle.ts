import Puzzle from '../../types/AbstractPuzzle';
import Directory from './Directory';
import { sum } from '../../utils/arrays';
import File from './File';

export default class ConcretePuzzle extends Puzzle {
    private RootCommand = 'cd /';
    private GoUpCommand = 'cd ..';
    private ListCommand = 'ls';

    public solveFirst(input: string): string {
        const maxDirSize = 100000;
        const root = new Directory('/');
        const objects: Directory[] = [root];

        const commands = input.split('\n$ ').slice(1);
        let currentNode: Directory = root;

        commands.forEach(output => {
            const outputLines = output.split('\n');
            const command = outputLines[0];
            // console.log(`Command: ${command}`);

            if (command === this.RootCommand) {
                console.log(`Going back to root`);
                currentNode = root;
            } else if (command === this.GoUpCommand) {
                if (currentNode.parent === undefined) {
                    console.log('Trying to go above the root dir, defaulting to root');
                    currentNode = root;
                } else {
                    currentNode = currentNode.getParent();
                    // console.log(`Going back to the parent directory`);
                }
            } else if (command === this.ListCommand) {
                // Create new repositories
                const dirNames = outputLines
                    .filter(line => line.startsWith('dir '))
                    .map(line => line.split(' ')[1]);
                const dirs: Directory[] = dirNames.map(name => {
                    const dir = new Directory(name, currentNode);
                    objects.push(dir);
                    return dir;
                });
                dirs.forEach(dir => currentNode.addSubdirectory(dir));

                // Create new files
                outputLines
                    .slice(1)
                    .filter(line => !line.startsWith('dir '))
                    .map(line => {
                        const [size, name] = line.split(' ');
                        // console.log(`Creating a new file ${name}`);
                        return new File(name, parseInt(size));
                    })
                    .forEach(file => {
                        currentNode.addFile(file);
                    });
            } else {
                // Change the repository to a different one
                const dirName = command.split(' ')[1];
                currentNode = objects.filter(d => d.name === dirName)[0];
                // console.log(`Changing directory to ${dirName}`);
            }
        });

        // console.log(objects);

        const smallDirSizeSum = sum(
            Array.from(objects.values())
                .map(d => d.getSize())
                .filter(size => size <= maxDirSize)
        );
        return smallDirSizeSum.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '95437';
    }

    public solveSecond(input: string): string {
        // WRITE SOLUTION FOR TEST 2
        return 'day 1 solution 2';
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return 'day 1 solution 2';
    }
}

