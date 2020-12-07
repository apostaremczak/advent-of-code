from solutions.day_07_handy_haversacks import *

test_bags = read_puzzle_input("puzzle_inputs/day_07.txt")


def test_creating_bags_from_text_rule():
    light_red = Bag(
        "light red bags contain 1 bright white bag, 2 muted yellow bags."
    )
    assert light_red.name == "light red"
    expected_content = {
        "bright white": 1,
        "muted yellow": 2
    }
    assert light_red.contained_bags == expected_content

    faded_blue = Bag(
        "faded blue bags contain no other bags."
    )
    assert faded_blue.contained_bags == {}


def can_reach_shiny_gold():
    light_red = test_bags["light red"]
    assert can_reach_shiny_gold(light_red, test_bags)

    dark_orange = test_bags["dark orange"]
    assert can_reach_shiny_gold(dark_orange, test_bags)

    bright_white = test_bags["bright white"]
    assert can_reach_shiny_gold(bright_white, test_bags)

    faded_blue = test_bags["faded blue"]
    assert not can_reach_shiny_gold(faded_blue, test_bags)


def test_part_1():
    assert part_1(test_bags) == 4


def test_count_children_bags():
    faded_blue = test_bags["faded blue"]
    assert count_children_bags(faded_blue, test_bags) == 0

    dark_olive = test_bags["dark olive"]
    assert count_children_bags(dark_olive, test_bags) == 7


def test_part_2():
    assert part_2(test_bags) == 32
