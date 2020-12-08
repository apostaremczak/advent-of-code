import re
from dataclasses import dataclass
from typing import Dict, List, Callable, Tuple


@dataclass
class Rule:
    op_name: str
    arg: int


@dataclass
class State:
    index: int
    accumulator: int


ACC = "acc"
JMP = "jmp"
NOP = "nop"
OPERATIONS: Dict[str, Callable[[int, State], State]] = {
    ACC: lambda arg, state: State(state.index + 1, state.accumulator + arg),
    JMP: lambda arg, state: State(state.index + arg, state.accumulator),
    NOP: lambda arg, state: State(state.index + 1, state.accumulator)
}


def read_puzzle_input(input_file_path: str) -> List[Rule]:
    rules = []
    with open(input_file_path, "r") as f:
        for rule in f.readlines():
            matches = re.match(r"^(\D{3}) ([+\-]\d+)$", rule)
            op_name, arg = matches.groups()
            rules.append(Rule(op_name, int(arg)))
    return rules


def execute_program(rules: List[Rule]) -> Tuple[State, bool]:
    """
    Returns the final step and a boolean indicating whether the end of
    the program was reached.
    """
    visited_indices = set()
    state = State(0, 0)
    visited_twice = False
    last_index = len(rules) - 1

    while not visited_twice and state.index <= last_index:
        visited_indices.add(state.index)
        rule = rules[state.index]
        state = OPERATIONS[rule.op_name](rule.arg, state)
        visited_twice = state.index in visited_indices
    return state, state.index >= last_index


def part_1(rules: List[Rule]) -> int:
    state, _ = execute_program(rules)
    return state.accumulator


def modify_rule(rules: List[Rule], at: int, new_rule_name: str) -> List[Rule]:
    return rules[:at] + [Rule(new_rule_name, rules[at].arg)] + rules[at + 1:]


def find_last_state(rules: List[Rule],
                    indices_to_modify: List[int],
                    new_rule_name: str) -> State:
    for i in indices_to_modify:
        new_rules = modify_rule(rules, i, new_rule_name)
        final_state, reached_end = execute_program(new_rules)
        if reached_end:
            return final_state


def part_2(rules: List[Rule]) -> int:
    jmp_indices = [i for i, rule in enumerate(rules) if rule.op_name == JMP]
    nop_indices = [i for i, rule in enumerate(rules) if rule.op_name == NOP]

    changed_jmp = find_last_state(rules, jmp_indices, NOP)
    if changed_jmp is not None:
        return changed_jmp.accumulator

    return find_last_state(rules, nop_indices, JMP).accumulator


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_08.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
