from numpy import median
from typing import List

OPENING = ("(", "[", "<", "{")
CLOSING = (")", "]", ">", "}")
MATCHING = {'(': ')', '[': ']', '<': '>', '{': '}'}
CORRUPTION_SCORING = {
    ")": 3,
    "]": 57,
    "}": 1197,
    ">": 25137
}
COMPLETION_SCORING = {
    ")": 1,
    "]": 2,
    "}": 3,
    ">": 4
}


def read_puzzle_input(input_file_path: str) -> List[str]:
    with open(input_file_path, "r") as f:
        return [x.strip() for x in f.readlines()]


def get_corruption_score(line: str) -> int:
    if not line or \
            not any(closing in line for closing in CLOSING) or \
            not any(opening in line for opening in OPENING):
        return 0

    for i, char in enumerate(line[:-1]):
        next_char = line[i + 1]
        if char in OPENING and next_char in CLOSING:
            if MATCHING[char] != next_char:
                return CORRUPTION_SCORING[next_char]
            return get_corruption_score(line[:i] + line[i + 2:])


def part_1(entries: List[str]) -> int:
    return sum(get_corruption_score(x) for x in entries)


def get_completion_score(ending: str) -> int:
    score = 0
    for letter in ending:
        score *= 5
        score += COMPLETION_SCORING[letter]
    return score


def get_unmatched_chars(line: str) -> str:
    if not any(closing in line for closing in CLOSING) or \
            not any(opening in line for opening in OPENING):
        return line

    for i, char in enumerate(line[:-1]):
        next_char = line[i + 1]
        if char in OPENING and next_char in CLOSING:
            if MATCHING[char] == next_char:
                return get_unmatched_chars(line[:i] + line[i + 2:])


def part_2(entries: List[str]) -> int:
    incomplete_lines = [
        line
        for line in entries
        if get_corruption_score(line) == 0
    ]

    completion_scores = []
    for line in incomplete_lines:
        unmatched_chars = get_unmatched_chars(line)
        ending = "".join([MATCHING[char] for char in unmatched_chars[::-1]])
        completion_scores.append(get_completion_score(ending))

    return int(median(completion_scores))


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_10.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
