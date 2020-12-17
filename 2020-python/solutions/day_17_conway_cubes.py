from collections import Counter
from itertools import product
from typing import Set, Tuple

Coordinates = Tuple[int, ...]


def read_puzzle_input(input_file_path: str) -> Set[Coordinates]:
    active_cubes = set()
    with open(input_file_path, "r") as f:
        for x, line in enumerate(f.readlines()):
            for y, char in enumerate(line):
                if char == "#":
                    active_cubes.add((x, y, 0))
    return active_cubes


def get_neighbors(cube: Coordinates) -> Set[Coordinates]:
    neighbors = {
        tuple(map(sum, zip(cube, delta)))
        for delta in product((-1, 0, 1), repeat=len(cube))
    }
    return neighbors - {cube}


def run_cycles(active_cubes: Set[Coordinates],
               num_cycles: int = 6) -> Set[Coordinates]:
    for cycle in range(num_cycles):
        new_active = set()
        inactive_cubes_counts = Counter()

        for cube in active_cubes:
            neighbors = get_neighbors(cube)
            inactive_neighbors = neighbors - active_cubes
            inactive_cubes_counts.update(n for n in inactive_neighbors)
            active_neighbors_count = len(neighbors) - len(inactive_neighbors)
            if active_neighbors_count in (2, 3):
                new_active.add(cube)

        for cube, active_neighbors_count in inactive_cubes_counts.items():
            if active_neighbors_count == 3:
                new_active.add(cube)

        active_cubes = new_active.copy()

    return active_cubes


def part_1(initial_active_cubes: Set[Coordinates]) -> int:
    return len(run_cycles(initial_active_cubes))


def part_2(initial_active_cubes: Set[Coordinates]) -> int:
    active_cubes_4d = {(x, y, z, 0) for x, y, z in initial_active_cubes}
    return len(run_cycles(active_cubes_4d))


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_17.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
