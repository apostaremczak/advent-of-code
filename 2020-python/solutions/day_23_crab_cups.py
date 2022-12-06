from dataclasses import dataclass
from typing import List


class CrabCupsGameState:
    def __init__(self, current_index: int, cups: List[int]):
        self.current_index = current_index
        self.cups = cups

    def get_next_to(self, index: int) -> int:
        return self.cups[index + 1 % len(self.cups)]

    def find_destination_value_within_range(self, current_value: int, start_idx: int, end_idx: int) -> int:
        candidates = self.get_slice(start_idx, end_idx)
        return max([c for c in candidates if c < current_value])

    def __getitem__(self, given):
        if isinstance(given, slice):
            if slice.start >= len(self.cups):
                return
            start_idx = slice.start

    def get_slice(self, start_idx: int, end_idx: int):
        """Get a slice of the current cups state. The range is inclusive."""
        max_idx = len(self.cups) - 1
        # The entire slice is within the array
        if start_idx <= end_idx <= max_idx:
            return self.cups[start_idx:end_idx + 1]
        # The slice starts at the end of an array and then wraps around
        if start_idx > end_idx:
            first_half = self.cups[start_idx:]
            second_half = self.cups[:end_idx - len(self.cups) + 1]
            return first_half + second_half
        # The entire slice is outside the array
        return self.get_slice(start_idx - len(self.cups), end_idx - len(self.cups))

    def simulate_move(self):
        current_cup = self.cups[self.current_index]
        picked_up = self.get_slice(self.current_index + 1, self.current_index + 3)
        cups_left_no_current = self.get_slice(self.current_index + 4, self.current_index - 1)
        potential_destination = [c for c in cups_left_no_current if c < current_cup]
        if potential_destination:
            destination_value = max(potential_destination)
        else:
            destination_value = max(cups_left_no_current)

        # The crab places the cups it just picked up so that they are immediately clockwise of the destination cup.
        # They keep the same order as when they were picked up.
        # Available: everything except the picked up
        cups_left_with_current = self.get_slice(self.current_index + 4, self.current_index)
        destination_idx = cups_left_with_current.index(destination_value)
        before = cups_left_with_current[:destination_idx + 1]
        after = cups_left_with_current[destination_idx + 1:]
        rearranged = before + picked_up + after

        next_index = (rearranged.index(current_cup) + 1) % len(self.cups)
        return CrabCupsGameState(
            current_index=next_index,
            cups=rearranged
        )

    def __eq__(self, other):
        # Two game states are considered the same if their current selected values are the same
        # and their alignment (read clockwise) is the same
        ordered = self.get_slice(self.current_index, self.current_index - 1)
        other_ordered = other.get_slice(other.current_index, other.current_index - 1)
        return ordered == other_ordered

    def __repr__(self):
        return f"CrabCupsGameState(current_index={self.current_index}, cups={self.cups})"

    def get_cups_after_1(self):
        one_idx = self.cups.index(1)
        return self.get_slice(one_idx + 1, one_idx - 1)


def parse_puzzle_input(labelling: int) -> List[int]:
    return [int(x) for x in str(labelling)]


def part_1(entries: List[int]) -> str:
    game_state = CrabCupsGameState(current_index=0, cups=entries)
    for _ in range(100):
        game_state = game_state.simulate_move()
    return ''.join([str(x) for x in game_state.get_cups_after_1()])


def part_2(entries: List[int]) -> str:
    pass


if __name__ == '__main__':
    puzzle_input = parse_puzzle_input(364297581)
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
