import re
from typing import Dict, List, Tuple, Set

InputValue = List[str]
OutputValue = List[str]
Entry = Tuple[InputValue, OutputValue]

DIGITS = {
    "abcefg": "0",
    "cf": "1",
    "acdeg": "2",
    "acdfg": "3",
    "bcdf": "4",
    "abdfg": "5",
    "abdefg": "6",
    "acf": "7",
    "abcdefg": "8",
    "abcdfg": "9"
}


def read_puzzle_input(input_file_path: str) -> List[Entry]:
    entries: List[Entry] = []
    with open(input_file_path, "r") as f:
        for entry_line in f.readlines():
            input_, output = re.match(r"^(.*) \| (.*)$", entry_line).groups()
            entries.append((input_.split(), output.split()))
    return entries


def part_1(entries: List[Entry]) -> int:
    return sum([
        sum([len(output) in (2, 4, 3, 7) for output in output_values])
        for _, output_values in entries
    ])


def find_symbol_map(input_words: List[str]) -> Dict[str, str]:
    def get_letters(*lengths: int) -> Set[str]:
        return set.intersection(*[length_letters[x] for x in lengths])

    def filter_letter(words: List[Set[str]], candidates: Set[str]) -> str:
        return set(filter(lambda letter: all(letter in w for w in words),
                          candidates)).pop()

    # Word length: [{letters in each of those words}]
    length_words = {
        n: [set(word) for word in input_words if len(word) == n]
        for n in (2, 3, 4, 5, 6)
    }

    # Word length: {letters used in words of this particular length}
    length_letters = {
        n: set().union(*w)
        for n, w in length_words.items()
    }

    # 'a' is present only in words of length 3, 5 or 6
    a = (get_letters(3, 5, 6) - get_letters(2, 6)).pop()

    # 'b' and 'd' are only present in words of length 4, 5 and 6
    b_d_letters = get_letters(4, 5, 6) - get_letters(2, 3)
    # 'd' can also occur on its own in words of length 5
    d = filter_letter(length_words[5], b_d_letters)
    b = (b_d_letters - {d}).pop()

    # 'e' and 'g' are only present in words of length 5, 6 or 7
    e_g_letters = get_letters(5, 6) - get_letters(2, 3, 4) - {a, b, d}
    # g can also occur on its own in words of length 5 and 6
    g = filter_letter(length_words[6], e_g_letters)
    e = (e_g_letters - {g}).pop()

    # Decode symbols 'c' and 'f'
    c_f_letters = get_letters(2)
    f = filter_letter(length_words[6], c_f_letters)
    c = (c_f_letters - {f}).pop()

    return {a: "a", b: "b", c: "c", d: "d", e: "e", f: "f", g: "g"}


def decode_number(encoded_digits: OutputValue,
                  symbol_map: Dict[str, str]) -> int:
    return int("".join([
        DIGITS["".join(sorted([symbol_map[s] for s in digit]))]
        for digit in encoded_digits
    ]))


def part_2(entries: List[Entry]) -> int:
    output_sum = 0
    for input_values, output_values in entries:
        symbol_map = find_symbol_map(input_values + output_values)
        output_sum += decode_number(output_values, symbol_map)
    return output_sum


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_08.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
