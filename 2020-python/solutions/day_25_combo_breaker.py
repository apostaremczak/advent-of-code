from sympy.ntheory.residue_ntheory import discrete_log
from typing import Tuple


def read_puzzle_input(input_file_path: str) -> Tuple[int]:
    with open(input_file_path, "r") as f:
        return tuple(int(x) for x in f.readlines())


def part_1(card_public_key: int, door_public_key: int) -> int:
    card_loop_size = discrete_log(20201227, card_public_key, 7)
    card_encryption_key = pow(door_public_key, card_loop_size, 20201227)
    return card_encryption_key


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_25.csv")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
