from typing import List


def parse_puzzle_input(labelling: int) -> List[int]:
    return [int(x) for x in str(labelling)]


def simulate_move(cups: List[int], current_cup_index: int = 0):
    current_cup = cups[current_cup_index]
    picked_up = cups[current_cup_index + 1:current_cup_index + 4]
    cups_left = cups[current_cup_index + 4:]
    destination_cup = cups_left.pop(current_cup - 1) \
        if current_cup - 1 in cups_left \
        else cups_left.pop(max(cups_left))
    rearranged = [current_cup] + [destination_cup] + cups_left



def part_1(entries: List[str]) -> int:
    pass


def part_2(entries: List[str]) -> int:
    pass


if __name__ == '__main__':
    puzzle_input = parse_puzzle_input(364297581)
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
