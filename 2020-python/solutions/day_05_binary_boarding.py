from typing import List


class BoardingPass:
    def __init__(self, seat_code: str):
        seat_code_binary = seat_code \
            .replace("F", "0") \
            .replace("L", "0") \
            .replace("B", "1") \
            .replace("R", "1")
        self.seat_id = int(seat_code_binary, 2)


def read_puzzle_input(input_file_path: str) -> List[BoardingPass]:
    with open(input_file_path, "r") as f:
        return [BoardingPass(x) for x in f.readlines()]


def part_1(boarding_passes: List[BoardingPass]) -> int:
    return max(b_pass.seat_id for b_pass in boarding_passes)


def part_2(boarding_passes: List[BoardingPass]) -> int:
    seat_ids = {b_pass.seat_id for b_pass in boarding_passes}
    seat_id_range = set(range(min(seat_ids), max(seat_ids)))
    return seat_id_range.difference(seat_ids).pop()


if __name__ == '__main__':
    boarding_passes_entries = read_puzzle_input("../puzzle_inputs/day_05.txt")
    print(f"Solution to part 1: {part_1(boarding_passes_entries)}")
    print(f"Solution to part 2: {part_2(boarding_passes_entries)}")
