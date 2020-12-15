from typing import List


def play_game(start_numbers: List[int], num_turns: int = 2020) -> int:
    numbers_mentioned = {
        number: i + 1 for i, number in enumerate(start_numbers[:-1])
    }
    last_spoken = start_numbers[-1]

    for turn in range(len(start_numbers), num_turns):
        last_spoken_turn = numbers_mentioned.get(last_spoken, turn)
        last_spoken_age = turn - last_spoken_turn
        numbers_mentioned[last_spoken] = turn
        last_spoken = last_spoken_age

    return last_spoken


def part_1(entries: List[int]) -> int:
    return play_game(entries)


def part_2(entries: List[int]) -> int:
    return play_game(entries, 30000000)


if __name__ == '__main__':
    puzzle_input = [12, 1, 16, 3, 11, 0]
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
