# --- Day 7: Recursive Circus ---
from random import choice

puzzle_input = {}
with open("day7/input.txt", "r") as file:
    for line in file:
        line = line.replace("\n", "")
        name, weight, *children = line.split(" ")
        if "->" in children:
            children.pop(children.index("->"))
        puzzle_input[name] = {
            "weight": int(weight[1:-1]),
            "children": [child.replace(",", "") for child in children]
        }


def grow_tree(tree):
    for name, details in tree.items():
        for child in details["children"]:
            tree[child]["parent"] = name
    return tree


def find_root(tree):
    # Start in a random place
    child = choice(list(tree.keys()))
    parent = tree[child].get("parent")

    while parent:
        child = parent
        parent = tree[child].get("parent")

    return child

print(f"The root of the tree is: {find_root(grow_tree(puzzle_input))}")


# --- Part Two ---

def find_leaves(tree, parent):
    leaves = []
    children = tree[parent].get("children", [])
    if children:
        for child in children:
            leaves.extend(find_leaves(tree, child))
    else:
        leaves.append(parent)
    return leaves


def find_balance(puzzle_input):
    tree = grow_tree(puzzle_input)
    leaves = find_leaves(tree, find_root(tree))
    level_weight = lambda parent: tree[parent]["weight"] \
                                  + sum(level_weight(child)
                                        for child in tree[parent]["children"])

    # Sprawdź wszystkie liście; przejdź do rodzica każdego z nich
    # i dla rodzica sprawdzaj, czy jest zbalansowany;
    for leave in leaves:
        parent_leave = tree[leave]["parent"]
        parent_weight = level_weight(parent_leave)
