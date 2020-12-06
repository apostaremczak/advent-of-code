def part_1(groups) -> int:
    return sum(len(set(group.replace("\n", ""))) for group in groups)


def part_2(groups) -> int:
    return sum(
        len(set.intersection(*[set(d) for d in group.split("\n") if d]))
        for group in groups
    )


if __name__ == '__main__':
    with open("../puzzle_inputs/day_06.txt", "r") as f:
        custom_groups = f.read().split("\n\n")
    print(f"Solution to part 1: {part_1(custom_groups)}")
    print(f"Solution to part 2: {part_2(custom_groups)}")
