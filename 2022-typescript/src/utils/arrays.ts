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
    return Array(end - start + 1).fill(0).map((_, idx) => start + idx);
}
