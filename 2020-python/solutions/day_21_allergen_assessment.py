import re
from typing import List, Tuple, Set


def read_puzzle_input(input_file_path: str) -> List[Tuple[str, str]]:
    ingredient_lists = []
    with open(input_file_path, "r") as f:
        for line in f.readlines():
            matches = re.findall(r"^(.*) \(contains (.*)\)$", line)
            ingredient_lists.extend(matches)
    return ingredient_lists


def find_allergens(entries: List[Tuple[str, str]]) -> Set[str]:
    unique_allergens = set()
    allergen_ingredients = {}
    for ingredients_str, allergens_str in entries:
        ingredient_allergens = set(allergens_str.split(", "))
        unique_allergens = unique_allergens.union(ingredient_allergens)
        ingredients = ingredients_str.split(" ")
        for al in ingredient_allergens:
            al_i = allergen_ingredients.get(al, [])
            al_i.append(ingredients)
            allergen_ingredients[al] = al_i



def part_1(entries: List[Tuple[str, str]]) -> int:
    pass


def part_2(entries: List[Tuple[str, str]]) -> int:
    pass


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_xx.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
