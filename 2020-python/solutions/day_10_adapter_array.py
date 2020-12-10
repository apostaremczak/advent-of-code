import re
from typing import List


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [int(x) for x in f.readlines()]


def get_joltage_diffs(adapters: List[int]) -> List[int]:
    adapters_sort = [0] + sorted(adapters) + [max(adapters) + 3]
    return [y - x for x, y in zip(adapters_sort, adapters_sort[1:])]


def part_1(adapters: List[int]) -> int:
    diffs = get_joltage_diffs(adapters)
    return diffs.count(1) * diffs.count(3)


def part_2(adapters: List[int]) -> int:
    diffs = get_joltage_diffs(adapters)
    diffs_char = "".join([str(d) for d in diffs])
    # Places where one adapter can be used or not (2 possibilities)
    pow_2 = len(re.findall(r"(?=(3113))", diffs_char))
    # Places where two adapters can be used or not (4 possibilities)
    pow_4 = len(re.findall(r"((?=(31113))|(^1113))", diffs_char))
    # Places where three adapters can be used or not (7 possibilities)
    pow_7 = diffs_char.count("1111")
    # Total number of adapter arrangements
    return 2 ** pow_2 * 4 ** pow_4 * 7 ** pow_7


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_10.csv")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
