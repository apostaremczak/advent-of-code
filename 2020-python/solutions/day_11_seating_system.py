import numpy as np
from typing import List, Dict, Tuple

Position = Tuple[int, int]
SeatDict = Dict[Position, str]

VISIBLE_DIRECTIONS = ((1, 1), (-1, 1), (0, 1), (1, 0), (-1, -1), (1, -1),
                      (0, -1), (-1, 0))


def read_puzzle_input(input_file_path: str) -> SeatDict:
    with open(input_file_path, "r") as f:
        seat_rows = [x for x in f.readlines()]
    seats = {}
    for row_index, seat_row in enumerate(seat_rows):
        for col_index, seat in enumerate(seat_row):
            if seat not in (".", "\n"):
                seats[(row_index, col_index)] = seat
    return seats


def occupy_nonadjacent_seats(seats: SeatDict) -> SeatDict:
    changed = True
    while changed:
        updated_seats = seats.copy()
        for (row, col), seat in seats.items():
            occupied_nearby = sum(
                seats.get((row + row_i, col + col_i), "") == "#"
                for (row_i, col_i) in VISIBLE_DIRECTIONS
            )
            if seat == "#" and occupied_nearby >= 4:
                updated_seats[(row, col)] = "L"
            elif seat == "L" and occupied_nearby == 0:
                updated_seats[(row, col)] = "#"
            else:
                updated_seats[(row, col)] = seat
        changed = updated_seats != seats
        seats = updated_seats.copy()
    return seats


def part_1(seats: SeatDict) -> int:
    rearranged_seats = occupy_nonadjacent_seats(seats)
    return list(rearranged_seats.values()).count("#")


def get_visible_seats(seats: SeatDict) -> Dict[Position, List[Position]]:
    deltas = [np.array(x) for x in VISIBLE_DIRECTIONS]
    max_row = max(x[0] for x in seats.keys())
    max_col = max(x[1] for x in seats.keys())

    all_visible_seats = {}
    for position, seat in seats.items():
        position_np = np.array(position)
        visible_seats = []
        for delta in deltas:
            searched_ray = False
            radius = 1
            while not searched_ray:
                other_seat = tuple(position_np + delta * radius)
                if 0 <= other_seat[0] <= max_row \
                        and 0 <= other_seat[1] <= max_col:
                    if other_seat in seats:
                        visible_seats.append(other_seat)
                        searched_ray = True
                    radius += 1
                else:
                    searched_ray = True
        all_visible_seats[position] = visible_seats
    return all_visible_seats


def occupy_seats(seats: SeatDict) -> SeatDict:
    visible_seats = get_visible_seats(seats)
    changed = True
    while changed:
        updated_seats = seats.copy()
        for (row, col), seat in seats.items():
            occupied_nearby = sum(
                seats.get(neighbour, "") == "#"
                for neighbour in visible_seats[(row, col)]
            )
            if seat == "#" and occupied_nearby >= 5:
                updated_seats[(row, col)] = "L"
            elif seat == "L" and occupied_nearby == 0:
                updated_seats[(row, col)] = "#"
            else:
                updated_seats[(row, col)] = seat
        changed = updated_seats != seats
        seats = updated_seats.copy()
    return seats


def part_2(seats: SeatDict) -> int:
    rearranged_seats = occupy_seats(seats)
    return list(rearranged_seats.values()).count("#")


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_11.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
