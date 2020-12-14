from typing import List


def read_puzzle_input(input_file_path: str) -> List[str]:
    with open(input_file_path, "r") as f:
        return [x for x in f.readlines()]


def part_1(entries: List[str]) -> int:
    pass


def part_2(entries: List[str]) -> int:
    pass


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_xx.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
