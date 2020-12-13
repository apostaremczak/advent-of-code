from math import ceil
from sympy.ntheory.modular import solve_congruence
from typing import List, Tuple, Optional


def read_puzzle_input(input_file_path: str) -> Tuple[int, List[Optional[int]]]:
    with open(input_file_path, "r") as f:
        current_time, bus_ids = f.readlines()
    bus_ids = [
        int(bus_id)
        if bus_id != "x" else None
        for bus_id in bus_ids.strip().split(",")
    ]
    return int(current_time), bus_ids


def calculate_wait_time(current_time: int, bus_id: int) -> int:
    nearest_departure = ceil(current_time / bus_id) * bus_id
    return nearest_departure - current_time


def part_1(current_time: int, bus_ids: List[Optional[int]]) -> int:
    bus_ids = list(filter(lambda b: b is not None, bus_ids))
    waiting_times = [
        calculate_wait_time(current_time, bus_id)
        for bus_id in bus_ids
    ]
    min_time = min(waiting_times)
    earliest_bus_id = bus_ids[waiting_times.index(min_time)]
    return earliest_bus_id * min_time


def part_2(bus_ids: List[Optional[int]]) -> int:
    congruence_system = [
        (-i, bus_id)
        for i, bus_id in enumerate(bus_ids)
        if bus_id is not None
    ]
    time, _ = solve_congruence(*congruence_system)
    return time


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_13.txt")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input[1])}")
