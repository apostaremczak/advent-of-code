import numpy as np
from solutions.day_04_giant_squid import *

board_numbers = np.array([[14, 21, 17, 24, 4],
                          [10, 16, 15, 9, 19],
                          [18, 8, 23, 26, 20],
                          [22, 11, 13, 6, 5],
                          [2, 0, 12, 3, 7]])


def test_bingo_board():
    board = BingoBoard(board_numbers)
    # Board shouldn't start with a winning state
    assert board.is_bingo() is False

    # After the first five numbers are drawn, there are no winners
    drawn_numbers = [7, 4, 9, 5, 11]
    for num in drawn_numbers:
        board.mark(num)
    assert board.is_bingo() is False

    # After the next six numbers are drawn, there are still no winners
    drawn_numbers = [17, 23, 2, 0, 14, 21]
    for num in drawn_numbers:
        board.mark(num)
    assert board.is_bingo() is False

    # Finally, 24 is drawn and the board wins
    board.mark(24)
    assert board.is_bingo() is True

    # Check the sum of the unmarked numbers
    assert board.get_unmarked_sum() == 188


def test_reading_input():
    drawn_numbers, boards = read_puzzle_input("puzzle_inputs/day_04.txt")
    assert np.array_equal(
        np.array(drawn_numbers),
        np.array([7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6,
                  15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1])
    )

    assert boards[-1] == BingoBoard(board_numbers)


def test_part_1():
    drawn_numbers, boards = read_puzzle_input("puzzle_inputs/day_04.txt")
    assert part_1(drawn_numbers, boards) == 4512


def test_part_2():
    drawn_numbers, boards = read_puzzle_input("puzzle_inputs/day_04.txt")
    assert part_2(drawn_numbers, boards) == 1924
