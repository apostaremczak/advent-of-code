from typing import List


def partition(low: int, high: int, directions: str) -> int:
    if low == high:
        return low
    mid = low + (high - low) // 2
    if directions[0] in ("F", "L"):
        return partition(low, mid, directions[1:])
    return partition(mid + 1, high, directions[1:])


class BoardingPass:
    ROW_RANGE = (0, 127)
    COLUMN_RANGE = (0, 7)

    def __init__(self, seat_code: str):
        row_code = seat_code[:7]
        col_code = seat_code[7:]
        self.row = partition(*BoardingPass.ROW_RANGE,
                             directions=row_code)
        self.column = partition(*BoardingPass.COLUMN_RANGE,
                                directions=col_code)
        self.seat_id = self.row * 8 + self.column


def read_puzzle_input(input_file_path: str) -> List[BoardingPass]:
    with open(input_file_path, "r") as f:
        return [
            BoardingPass(x)
            for x in f.readlines()
        ]


def part_1(boarding_passes: List[BoardingPass]) -> int:
    return max(b_pass.seat_id for b_pass in boarding_passes)


def part_2(boarding_passes: List[BoardingPass]) -> int:
    seat_ids = {b_pass.seat_id for b_pass in boarding_passes}
    seat_id_range = set(range(min(seat_ids), max(seat_ids)))
    missing_seats = seat_id_range.difference(seat_ids)
    assert len(missing_seats) == 1
    return missing_seats.pop()


if __name__ == '__main__':
    boarding_passes_entries = read_puzzle_input("../puzzle_inputs/day_05.txt")
    print(f"Solution to part 1: {part_1(boarding_passes_entries)}")
    print(f"Solution to part 2: {part_2(boarding_passes_entries)}")
