export function sortDesc(nums: number[]): number[] {
    return nums.sort((one, two) => (one > two ? -1 : 1));
}

export function sum(nums: number[]): number {
    return nums.reduce((sum, current) => sum + current, 0)
}

