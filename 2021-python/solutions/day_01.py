from typing import List


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [int(x) for x in f.readlines()]


def part_1(entries: List[int]) -> int:
    return sum([first < second
                for first, second in zip(entries, entries[1:])])


def part_2(entries: List[int]) -> int:
    triplet_sums = [
        x + y + z
        for x, y, z in zip(entries, entries[1:], entries[2:])
    ]
    return part_1(triplet_sums)


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_01.csv")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
