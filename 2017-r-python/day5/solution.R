# --- Day 5: A Maze of Twisty Trampolines, All Alike ---

puzzle_input <- read.csv("input.txt", sep = "\n", header = FALSE)


instructions <- puzzle_input[,1]
exit_index <- length(instructions)
step_count <- 0
current_index <- 1

while(current_index <= exit_index) {
    step_count <- step_count + 1
    next_index <- current_index + instructions[current_index]
    instructions[current_index] <- instructions[current_index] + 1
    current_index <- next_index
}


# --- Part Two ---

instructions <- puzzle_input[,1]
exit_index <- length(instructions)
step_count <- 0
current_index <- 1

while(current_index <= exit_index) {
    step_count <- step_count + 1
    next_index <- current_index + instructions[current_index]
    if (instructions[current_index] < 3) instructions[current_index] <- instructions[current_index] + 1
    else instructions[current_index] <- instructions[current_index] - 1
    current_index <- next_index
}
