export function sortDesc(nums: number[]): number[] {
    return nums.sort((one, two) => (one > two ? -1 : 1));
}

export function sum(nums: number[]): number {
    return nums.reduce((sum, current) => sum + current, 0);
}

export function deduplicate<T>(arr: T[]): T[] {
    return Array.from(new Set(arr));
}
