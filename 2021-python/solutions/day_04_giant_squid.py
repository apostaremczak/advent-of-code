import numpy as np
from copy import deepcopy
from typing import List, Tuple


class BingoBoard:
    def __init__(self, board: np.ndarray):
        self.board: np.ndarray = board

    def is_bingo(self) -> bool:
        return self.board.shape == (4, 5) or self.board.shape == (5, 4)

    def mark(self, number: int) -> None:
        updated_board = np.where(self.board == number, -1, self.board)
        if not np.array_equal(self.board, updated_board):
            row_idx, col_idx = np.where(self.board == number)
            self.board = updated_board
            row = self.board[row_idx]
            if np.count_nonzero(row == -1) == 5:
                self.board = np.delete(self.board, row_idx, axis=0)
            col = self.board[:, col_idx]
            if np.count_nonzero(col == -1) == 5:
                self.board = np.delete(self.board, col_idx, axis=1)

    def get_unmarked_sum(self) -> int:
        return int(np.sum(np.where(self.board == -1, 0, self.board)))

    def __str__(self):
        return str(self.board)

    def __eq__(self, other):
        return np.array_equal(self.board, other.board)


def _parse_board(board_representation: str) -> BingoBoard:
    board_nums = [
        int(x.strip())
        for line in board_representation.split("\n")
        for x in line.split(" ")
        if x
    ]
    return BingoBoard(np.array(board_nums).reshape(5, 5))


def read_puzzle_input(input_file_path: str) -> Tuple[List[int], List[BingoBoard]]:
    with open(input_file_path, "r") as f:
        drawn_numbers = [int(x) for x in f.readline().split(",")]
        f.readline()
        boards = [_parse_board(x) for x in f.read().split("\n\n")]

    return drawn_numbers, boards


def part_1(drawn_numbers: List[int], boards: List[BingoBoard]) -> int:
    for number in drawn_numbers:
        for board in boards:
            board.mark(number)
            if board.is_bingo():
                return number * board.get_unmarked_sum()


def part_2(drawn_numbers: List[int], boards: List[BingoBoard]) -> int:
    boards_in_game = set(range(len(boards)))
    for number in drawn_numbers:
        next_round_boards = deepcopy(boards_in_game)
        for board_idx in boards_in_game:
            board = boards[board_idx]
            board.mark(number)
            if board.is_bingo():
                next_round_boards.remove(board_idx)
            if not len(next_round_boards):
                return number * board.get_unmarked_sum()
        boards_in_game = next_round_boards


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_04.txt")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
    print(f"Solution to part 2: {part_2(*puzzle_input)}")
