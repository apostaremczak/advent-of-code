from itertools import islice, tee, combinations
from typing import List, Tuple, Iterable


def window(iterable: Iterable[int], size: int) -> List[Tuple[int]]:
    iterators = tee(iterable, size)
    iterators = [islice(iterator, i, None)
                 for i, iterator in enumerate(iterators)]
    yield from zip(*iterators)


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [int(x) for x in f.readlines()]


def is_sum(candidates: Iterable[int], goal: int) -> bool:
    for x, y in combinations(candidates, 2):
        if x + y == goal:
            return True
    return False


def find_first_misfit(numbers: List[int], window_size: int) -> int:
    for number_window in window(numbers, window_size + 1):
        x = number_window[-1]
        if not is_sum(number_window[:-1], x):
            return x


def find_sum_range(numbers: List[int], goal: int) -> Tuple[int, int]:
    goal_ind = numbers.index(goal)
    for window_size in range(2, goal_ind):
        for number_window in window(numbers, window_size):
            if sum(number_window) == goal:
                return min(number_window), max(number_window)


def part_1(numbers: List[int]) -> int:
    return find_first_misfit(numbers, 25)


def part_2(numbers: List[int], goal: int) -> int:
    return sum(find_sum_range(numbers, goal))


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_09.csv")
    solution_1 = part_1(puzzle_input)
    print(f"Solution to part 1: {solution_1}")
    print(f"Solution to part 2: {part_2(puzzle_input, solution_1)}")
