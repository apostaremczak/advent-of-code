import re
from collections import Counter, defaultdict
from typing import Dict, List, Tuple, Set

Ingredient = str
Allergen = str
Food = Tuple[List[Ingredient], List[Allergen]]


def read_puzzle_input(input_file_path: str) -> List[Food]:
    food_list = []
    with open(input_file_path, "r") as f:
        for line in f.readlines():
            matches = re.match(r"^(.*) \(contains (.*)\)$", line).groups()
            ingredients_str, allergens_str = matches
            food_list.append((ingredients_str.split(),
                              allergens_str.split(", ")))
    return food_list


def get_potentially_allergic_ingredients(food_list: List[Food]) -> Dict[Allergen, Set[Ingredient]]:
    allergen_ingredients = {}
    for ingredients, allergens in food_list:
        for allergen in allergens:
            allergen_ingredients[allergen] = allergen_ingredients.get(allergen, set(ingredients)) & set(ingredients)
    return allergen_ingredients


def part_1(food_list: List[Food]) -> int:
    ingredient_counts = Counter()
    for ingredients, _ in food_list:
        ingredient_counts.update(ingredients)

    allergen_ingredients = get_potentially_allergic_ingredients(food_list)
    unsafe_ingredients = set.union(*[v for v in allergen_ingredients.values()])
    return sum(
        count
        for ingredient, count in ingredient_counts.items()
        if ingredient not in unsafe_ingredients
    )


def part_2(food_list: List[Food]) -> str:
    potential_allergens = get_potentially_allergic_ingredients(food_list)
    canonical_allergens: Dict[Allergen, Ingredient] = {}

    while potential_allergens:
        allergen, ingredients = min(potential_allergens.items(),
                                    key=lambda x: len(x[1]))
        potential_allergens = {
            a: i.difference(ingredients)
            for a, i in potential_allergens.items()
        }
        potential_allergens.pop(allergen)

        ingredient = ingredients.pop()
        canonical_allergens[allergen] = ingredient

    return ",".join([
        ingredient for _, ingredient in sorted(canonical_allergens.items())
    ])


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_21.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
