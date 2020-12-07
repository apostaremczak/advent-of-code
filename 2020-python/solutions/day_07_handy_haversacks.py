import re
from typing import Dict

SHINY_GOLD = "shiny gold"


class Bag:
    def __init__(self, rule: str):
        matches = re.match(r"(.+) bags contain (.+)", rule)
        bag_name, content_details = matches.groups()
        self.name = bag_name
        self.contained_bags = {}
        self.can_contain_gold = None
        for num_bags, name in re.findall(r"(\d) (\D+) bag", content_details):
            self.contained_bags[name] = int(num_bags)


def read_puzzle_input(input_file_path: str) -> Dict[str, Bag]:
    with open(input_file_path, "r") as f:
        bags = [Bag(line) for line in f.readlines()]
    return {bag.name: bag for bag in bags}


def can_reach_shiny_gold(current_bag: Bag, all_bags: Dict[str, Bag]) -> bool:
    if current_bag.can_contain_gold is not None:
        return current_bag.can_contain_gold

    for bag_name in current_bag.contained_bags:
        if bag_name == SHINY_GOLD \
                or can_reach_shiny_gold(all_bags[bag_name], all_bags):
            current_bag.can_contain_gold = True
            return True

    current_bag.can_contain_gold = False
    return False


def part_1(all_bags: Dict[str, Bag]) -> int:
    return sum(
        can_reach_shiny_gold(bag, all_bags)
        for bag in all_bags.values()
        if bag.name != SHINY_GOLD
    )


def count_children_bags(current_bag: Bag, all_bags: Dict[str, Bag]) -> int:
    return sum(
        bag_count * (1 + count_children_bags(all_bags[bag_name], all_bags))
        for bag_name, bag_count in current_bag.contained_bags.items()
    )


def part_2(all_bags: Dict[str, Bag]) -> int:
    shiny_gold_bag = all_bags[SHINY_GOLD]
    return count_children_bags(shiny_gold_bag, all_bags)


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_07.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
