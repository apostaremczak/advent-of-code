from math import prod
from itertools import combinations

from typing import List, Tuple


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [int(x) for x in f.readlines()]


def find_summands(candidates: List[int],
                  desired_sum: int,
                  num_of_summands: int = 2) -> List[Tuple[int]]:
    return list(filter(
        lambda xs: sum(xs) == desired_sum,
        combinations(candidates, r=num_of_summands)
    ))


def find_product_of_summands(candidates: List[int],
                             desired_sum: int,
                             num_of_summands: int) -> int:
    summands = find_summands(candidates, desired_sum, num_of_summands)
    if not summands:
        raise ValueError("No valid summands found")
    if len(summands) > 1:
        print("Found more than one summand candidates, using the first ones")

    return prod(summands[0])


def part_1(puzzle_entry: List[int]) -> int:
    """
    Find the product of two numbers than sum to 2020
    """
    return find_product_of_summands(puzzle_entry, 2020,  2)


def part_2(puzzle_entry: List[int]) -> int:
    """
    Find the product of three numbers than sum to 2020
    """
    return find_product_of_summands(puzzle_entry, 2020,  3)


if __name__ == '__main__':
    summand_candidates = read_puzzle_input("puzzle_inputs/day_01.csv")
    print(f"Solution to part 1: {part_1(summand_candidates)}")
    print(f"Solution to part 2: {part_2(summand_candidates)}")
