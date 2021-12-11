from solutions.day_11_dumbo_octopus import *


def parse_string_input(board: str):
    return {
        (x, y): int(value)
        for x, line in enumerate(board.split("\n"))
        for y, value in enumerate(line.strip())
    }


def test_run_step_small():
    octopus_board = parse_string_input("""
    11111
    19991
    19191
    19991
    11111
    """)

    after_first_step = parse_string_input("""
    34543
    40004
    50005
    40004
    34543
    """)

    assert run_step(octopus_board)[0] == after_first_step

    after_second_step = parse_string_input("""
    45654
    51115
    61116
    51115
    45654
    """)

    assert run_step(after_first_step)[0] == after_second_step


def test_run_step_big():
    octopus_board = parse_string_input("""
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
    """)

    for _ in range(5):
        octopus_board, _ = run_step(octopus_board)

    assert octopus_board == parse_string_input("""
    4484144000
    2044144000
    2253333493
    1152333274
    1187303285
    1164633233
    1153472231
    6643352233
    2643358322
    2243341322
    """)


def test_part_1():
    octopuses = parse_string_input("""
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
    """)

    assert part_1(octopuses) == 1656

