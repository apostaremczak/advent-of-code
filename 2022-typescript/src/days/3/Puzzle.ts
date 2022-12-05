import Puzzle from '../../types/AbstractPuzzle';

export default class ConcretePuzzle extends Puzzle {
    private splitInHalf(word: string): [string, string] {
        const middle = Math.floor(word.length / 2);
        const first = word.slice(0, middle);
        const second = word.slice(middle, word.length);
        return [first, second];
    }

    private intersection(first: string[], second: string[]): Set<string> {
        return new Set<string>(first.filter(letter => second.includes(letter)));
    }

    private alphabetUppercase = Array.from(Array(26)).map((_, i) => String.fromCharCode(i + 65));
    private alphabetLowercase = Array.from(Array(26)).map((_, i) => String.fromCharCode(i + 97));
    private priorities: Map<string, number> = new Map(
        this.alphabetLowercase.concat(this.alphabetUppercase).map((element, index) => {
            return [element, index + 1];
        })
    );

    public solveFirst(input: string): string {
        const rucksacks = input.split('\n');
        const commons: string[][] = rucksacks.map(r => {
            const [first, second] = this.splitInHalf(r);
            return Array.from(this.intersection(first.split(''), second.split('')));
        });

        const commonsPriorities = commons
            .reduce((prev, current) => prev.concat(current), [])
            .map(e => this.priorities.get(e));

        const result = this.sum(commonsPriorities);
        return result.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '157';
    }

    private sliceIntoChunks(array: string[], chunkSize: number): string[][] {
        return Array(Math.ceil(array.length / chunkSize)).fill([])
            .map((_, index) => index * chunkSize)
            .map(begin => array.slice(begin, begin + chunkSize));
    }

    public solveSecond(input: string): string {
        const rucksacks = input.split('\n');
        const elfGroups: string[][] = this.sliceIntoChunks(rucksacks, 3);
        const badges: string[] = elfGroups.map(group => {
            const [first, second, third] = group;
            const firstSecond = this.intersection(first.split(''), second.split(''));
            const badge = this.intersection(Array.from(firstSecond), third.split(''));
            return Array.from(badge)[0];
        });
        const badgePriorities = this.sum(badges.map(e => this.priorities.get(e)));

        return badgePriorities.toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '70';
    }
}
