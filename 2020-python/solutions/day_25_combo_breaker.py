from typing import Tuple


def read_puzzle_input(input_file_path: str) -> Tuple[int]:
    with open(input_file_path, "r") as f:
        return tuple(int(x) for x in f.readlines())


def determine_loop_size(subject_number: int, public_key: int) -> int:
    """
    Pohlig-Hellman would be nicer for finding discrete logarithms though
    """
    current_value = 1
    loop_size = 0
    while current_value != public_key:
        current_value = (current_value * subject_number) % 20201227
        loop_size += 1
    return loop_size


def transform_subject_number(subject_number: int,
                             loop_size: int,
                             current_value: int = 1) -> int:
    for _ in range(loop_size):
        current_value = (current_value * subject_number) % 20201227
    return current_value


def part_1(card_public_key: int, door_public_key: int) -> int:
    card_loop_size = determine_loop_size(7, card_public_key)
    card_encryption_key = transform_subject_number(door_public_key,
                                                   card_loop_size)
    return card_encryption_key


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_25.csv")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
