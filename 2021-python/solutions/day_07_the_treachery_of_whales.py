import numpy as np
from typing import List


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [int(x) for x in f.read().split(",")]


def part_1(entries: List[int]) -> int:
    target = int(np.median(entries))
    return sum([abs(i - target) for i in entries])


def fuel_cost(start, target) -> int:
    return (abs(start - target) * (abs(start - target) + 1)) // 2


def part_2(entries: List[int]) -> int:
    return min([
        sum([fuel_cost(x_i, t) for x_i in entries])
        for t in range(min(entries), max(entries) + 1)
    ])


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_07.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
