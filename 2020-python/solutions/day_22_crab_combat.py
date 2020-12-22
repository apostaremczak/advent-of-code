from typing import List, Tuple, Set

Deck = List[int]


def read_puzzle_input(input_file_path: str) -> Tuple[Deck, Deck]:
    with open(input_file_path, "r") as f:
        deck_1, deck_2 = f.read().split("\n\n")

    deck_1 = [int(x) for x in deck_1.strip().split("\n")[1:] if x]
    deck_2 = [int(x) for x in deck_2.strip().split("\n")[1:] if x]
    return deck_1, deck_2


def calculate_score(deck: Deck) -> int:
    return sum(
        i * card
        for i, card in enumerate(deck[::-1], start=1)
    )


def play_small_crab(deck_1: Deck, deck_2: Deck) -> Deck:
    while deck_1 and deck_2:
        card_1 = deck_1.pop(0)
        card_2 = deck_2.pop(0)

        if card_1 > card_2:
            deck_1.extend([card_1, card_2])
        elif card_2 > card_1:
            deck_2.extend([card_2, card_1])
        else:
            raise ValueError("Tie!")
    return deck_1 or deck_2


def play_recursive_combat(deck_1: Deck, deck_2: Deck) -> Tuple[Deck, int]:
    prev_game_decks = set()

    while deck_1 and deck_2:
        game_pair = (tuple(deck_1), tuple(deck_2))
        if game_pair in prev_game_decks:
            return deck_1, 1
        prev_game_decks.add(game_pair)
        card_1 = deck_1.pop(0)
        card_2 = deck_2.pop(0)

        if len(deck_1) >= card_1 and len(deck_2) >= card_2:
            _, w = play_recursive_combat(deck_1[:card_1], deck_2[:card_2])
            if w == 1:
                deck_1.extend([card_1, card_2])
            else:
                deck_2.extend([card_2, card_1])
        else:
            if card_1 > card_2:
                deck_1.extend([card_1, card_2])
            elif card_2 > card_1:
                deck_2.extend([card_2, card_1])
            else:
                raise ValueError("Tie!")

    winner = 1 if deck_1 else 2
    winning_deck = deck_1 or deck_2
    return winning_deck, winner


def part_1(deck_1: Deck, deck_2: Deck) -> int:
    return calculate_score(play_small_crab(deck_1, deck_2))


def part_2(deck_1: Deck, deck_2: Deck) -> int:
    return calculate_score(play_recursive_combat(deck_1, deck_2)[0])


if __name__ == '__main__':
    player_1, player_2 = read_puzzle_input("../puzzle_inputs/day_22.txt")
    print(f"Solution to part 1: {part_1(player_1[::], player_2[::])}")
    print(f"Solution to part 2: {part_2(player_1[::], player_2[::])}")
