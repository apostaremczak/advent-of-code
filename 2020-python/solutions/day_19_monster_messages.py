import functools
import re
from itertools import product
from typing import Dict, List, Set


def read_puzzle_input(input_file_path: str):
    with open(input_file_path, "r") as f:
        rules, messages = f.read().split("\n\n")

    rule_dict = {}
    messages = messages.strip().split("\n")

    for rule in rules.strip().split("\n"):
        matches = re.match(r"^(\d+): (.*)$", rule)
        num, matching_rules = matches.groups()
        alternatives = matching_rules.split(" | ")
        rule_dict[int(num)] = alternatives

    return rule_dict, messages


def parse_all_messages(rule_dict: Dict[int, List[str]]) -> Set[str]:
    @functools.lru_cache
    def parse_message(num: int) -> Set[str]:
        alternatives = rule_dict[num]
        messages = set()
        for alt in alternatives:
            matching_rules = re.findall(r"(\d+)", alt)
            if not matching_rules:
                messages.add(re.findall(r'"(\w)"', alt)[0])
            else:
                rule_nums = [int(x) for x in matching_rules]
                matching_messages = [
                    parse_message(n)
                    for n in rule_nums
                ]
                messages = messages.union({
                    "".join(comb) for comb in product(*matching_messages)
                })
        return messages

    return parse_message(0)


def part_1(rule_dict: Dict[int, List[str]], messages: List[str]) -> int:
    return len(parse_all_messages(rule_dict).intersection(set(messages)))


def part_2(rule_dict: Dict[int, List[str]], messages: List[str]) -> int:
    updated_rules = rule_dict.copy()
    updated_rules[8] = "42 | 8".split(" | ")
    updated_rules[11] = "42 31 | 42 11 31".split(" | ")
    return len(parse_all_messages(updated_rules).intersection(set(messages)))


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_19.txt")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
    # print(f"Solution to part 2: {part_2(*puzzle_input)}")
