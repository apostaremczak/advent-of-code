from itertools import product
from typing import Dict, Tuple, List, Set

Coord = Tuple[int, int]


def get_neighbors(octopus: Coord) -> Set[Coord]:
    neighbors = {
        tuple(map(sum, zip(octopus, delta)))
        for delta in product((-1, 0, 1), repeat=2)
    }
    return neighbors - {octopus}


def read_puzzle_input(input_file_path: str) -> Dict[Coord, int]:
    with open(input_file_path, "r") as f:
        return {
            (x, y): int(value)
            for x, line in enumerate(f.readlines())
            for y, value in enumerate(line.strip())
        }


def get_octopuses_to_flash(octopuses: Dict[Coord, int]) -> Set[Coord]:
    return {coord for coord, energy_level in octopuses.items()
            if energy_level > 9}


def run_step(octopuses: Dict[Coord, int]) -> Tuple[Dict[Coord, int], int]:
    new_octopus_state = {
        coord: energy_level + 1
        for coord, energy_level in octopuses.items()
    }

    flashing_octopuses = get_octopuses_to_flash(new_octopus_state)
    already_flashed: Set[Coord] = set()

    while flashing_octopuses:
        current = flashing_octopuses.pop()
        adjacent = get_neighbors(current)
        for neighbor in adjacent:
            if neighbor in new_octopus_state and neighbor not in already_flashed:
                new_octopus_state[neighbor] += 1
        new_octopus_state[current] = 0
        already_flashed |= {current}

        # Check if any new octopuses should flash next
        flashing_octopuses |= (
                get_octopuses_to_flash(new_octopus_state) - already_flashed
        )

    return new_octopus_state, len(already_flashed)


def part_1(octopuses: Dict[Coord, int]) -> int:
    total_flash_count = 0
    for _ in range(100):
        octopuses, step_flash_count = run_step(octopuses)
        total_flash_count += step_flash_count
    return total_flash_count


def part_2(octopuses: Dict[Coord, int]) -> int:
    octopus_count = len(octopuses)
    can_pass_safely = False
    current_step = 0

    while not can_pass_safely:
        octopuses, step_flash_count = run_step(octopuses)
        can_pass_safely = step_flash_count == octopus_count
        current_step += 1

    return current_step


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_11.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
