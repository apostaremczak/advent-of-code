from solutions.day_05_binary_boarding import *


def test_partition():
    assert partition(0, 127, "FBFBBFF") == 44
    assert partition(0, 7, "RLR") == 5


def test_seat_id():
    assert BoardingPass("FBFBBFFRLR").seat_id == 357
    assert BoardingPass("BFFFBBFRRR").seat_id == 567
    assert BoardingPass("FFFBBBFRRR").seat_id == 119
    assert BoardingPass("BBFFBBFRLL").seat_id == 820


def test_part_1():
    passes = [
        BoardingPass("BFFFBBFRRR"),
        BoardingPass("FFFBBBFRRR"),
        BoardingPass("BBFFBBFRLL")
    ]
    assert part_1(passes) == 820
