export function sortDesc(nums: number[]): number[] {
    return nums.sort((one, two) => (one > two ? -1 : 1));
}

export function sum(nums: number[]): number {
    return nums.reduce((sum, current) => sum + current, 0);
}

export function deduplicate<T>(arr: T[]): T[] {
    return Array.from(new Set(arr));
}

export function zip<A, B>(a: A[], b: B[]): [A, B][] {
    return a.map((k, i) => [k, b[i]]);
}

export function rangeInclusive(start: number, end: number): number[] {
    if (start <= end) {
        return Array(end - start + 1).fill(0).map((_, idx) => start + idx);

    }
    // Reversed array
    return Array(start - end + 1).fill(0).map((_, idx) => end + idx);
}

export function toSlidingWindows<T>(inputArray: T[], windowSize: number): T[][] {
    return Array.from(
        { length: inputArray.length - (windowSize - 1) }, // get the appropriate length
        (_, index) => inputArray.slice(index, index + windowSize) // create the windows
    );
}

// https://stackoverflow.com/questions/9960908/permutations-in-javascript
export function permute<T>(inputArray: T[]): T[][] {
    const result: T[][] = [];

    const permute = (arr: T[], m: T[] = []) => {
        if (arr.length === 0) {
            result.push(m);
        } else {
            for (let i = 0; i < arr.length; i++) {
                const curr = arr.slice();
                const next = curr.splice(i, 1);
                permute(curr.slice(), m.concat(next));
            }
        }
    };

    permute(inputArray);
    return result;
}
