from solutions.day_23_crab_cups import *


def test_game_state_get_slice():
    game_state = CrabCupsGameState(current_index=0, cups=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9])

    assert game_state.get_slice(0, 4) == [0, 1, 2, 3, 4]
    assert game_state.get_slice(0, 0) == [0]

    assert game_state.get_slice(7, 1) == [7, 8, 9, 0, 1]

    assert game_state.get_slice(10, 13) == [0, 1, 2, 3]

    game_state_1 = CrabCupsGameState(current_index=1, cups=[3, 2, 8, 9, 1, 5, 4, 6, 7])
    assert game_state_1.get_slice(5, 0) == [5, 4, 6, 7, 3]


def test_simulate_move():
    game_state_0 = CrabCupsGameState(current_index=0, cups=parse_puzzle_input(389125467))

    game_state_1 = game_state_0.simulate_move()
    desired_state_1 = CrabCupsGameState(1, cups=[3, 2, 8, 9, 1, 5, 4, 6, 7])
    assert game_state_1 == desired_state_1

    game_state_2 = game_state_1.simulate_move()
    desired_state_2 = CrabCupsGameState(2, cups=[3, 2, 5, 4, 6, 7, 8, 9, 1])
    assert game_state_2 == desired_state_2

    game_state_3 = game_state_2.simulate_move()
    desired_state_3 = CrabCupsGameState(3, cups=[7, 2, 5, 8, 9, 1, 3, 4, 6])
    assert game_state_3 == desired_state_3


def test_simulate_multiple_moves():
    game_state = CrabCupsGameState(current_index=0, cups=parse_puzzle_input(389125467))
    for _ in range(10):
        game_state = game_state.simulate_move()
    desired_state = CrabCupsGameState(current_index=1, cups=[5, 8, 3, 7, 4, 1, 9, 2, 6])
    assert game_state == desired_state


def test_get_cups_after_1():
    game_state = CrabCupsGameState(current_index=1, cups=[5, 8, 3, 7, 4, 1, 9, 2, 6])
    assert game_state.get_cups_after_1() == [9, 2, 6, 5, 8, 3, 7, 4]


def test_part_1():
    test_input = parse_puzzle_input(389125467)
    solution = part_1(test_input)
    assert solution == '67384529'


def test_part_2():
    test_input = parse_puzzle_input(389125467)
    solution = part_2(test_input)
    assert solution == 149245887792
