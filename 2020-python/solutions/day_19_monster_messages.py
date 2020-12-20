import functools
import re
from typing import Dict, List


def read_puzzle_input(input_file_path: str):
    with open(input_file_path, "r") as f:
        rules, messages = f.read().split("\n\n")

    rule_dict = {}
    messages = messages.strip().split("\n")

    for rule in rules.strip().split("\n"):
        matches = re.match(r"^(\d+): (.*)$", rule)
        num, matching_rules = matches.groups()
        alternatives = matching_rules.split(" | ")
        rule_dict[num] = alternatives

    return rule_dict, messages


def create_regex(rules_dict: Dict[str, List[str]]) -> str:
    @functools.lru_cache()
    def regex_rec(rule_n: str) -> str:
        alternatives = rules_dict[rule_n]
        if len(alternatives) == 1:
            # Single letter
            letter_match = re.findall(r'"(\w)"', alternatives[0])
            if letter_match:
                return letter_match[0]
            # List of numbers
            matching_rules = re.findall(r"(\d+)", alternatives[0])
            return "".join(regex_rec(n) for n in matching_rules)
        # Consider all the alternatives
        alt_rules = [re.findall(r"(\d+)", alt) for alt in alternatives]
        regexes = [
            "".join(regex_rec(n) for n in rule)
            for rule in alt_rules
        ]
        return "(" + "|".join(regexes) + ")"
    return regex_rec("0")


def part_1(rule_dict: Dict[str, List[str]], messages: List[str]) -> int:
    regex = create_regex(rule_dict)
    return sum(
        re.fullmatch(regex, m) is not None
        for m in messages
    )


def part_2(rule_dict: Dict[str, List[str]], messages: List[str]) -> int:
    updated_rules = rule_dict.copy()
    updated_rules["8"] = [
        " ".join(["42"] * i)
        for i in range(1, 6)
    ]
    updated_rules["11"] = [
        " ".join(["42"] * i + ["31"] * i)
        for i in range(1, 6)
    ]
    regex = create_regex(updated_rules)

    return sum(
        re.fullmatch(regex, m) is not None
        for m in messages
    )


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_19.txt")
    print(f"Solution to part 1: {part_1(*puzzle_input)}")
    print(f"Solution to part 2: {part_2(*puzzle_input)}")
