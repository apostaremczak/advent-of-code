# --- Day 6: Memory Reallocation ---

puzzle_input <- read.csv("input.csv", sep = "\t", header = FALSE)
len <- ncol(puzzle_input)

row_to_string <- function(row) {
    return(paste(as.character(row), collapse = ","))
}

next_ind <- function(i) {
    if (i + 1 <= len) return(i + 1)
    else return(1)
}

redistribute <- function(row) {
    bank <- max(row[1:len])
    bank_ind <- which(row == bank)[1]
    row[,bank_ind] <- 0

    current_ind <- next_ind(bank_ind)
    while(bank) {
        row[,current_ind] <- row[,current_ind] + 1
        bank <- bank - 1
        current_ind <- next_ind(current_ind)
    }
    
    return(row)
}

puzzle_input[1, len + 1] <- row_to_string(puzzle_input[1,])
redistribution_count <- 0

while(!any(duplicated(puzzle_input[, len + 1]))) {
    last_row <- tail(puzzle_input, n=1)
    new_row <- redistribute(last_row[1:len])
    redistribution_count <- redistribution_count + 1
    puzzle_input[nrow(puzzle_input) + 1,] <- c(new_row, row_to_string(new_row))
}


# --- Part Two ---

last_conf <- puzzle_input[nrow(puzzle_input), len + 1]
duplicates <- which(puzzle_input[,len + 1] == last_conf)
how_many_loops <- duplicates[2] - duplicates[1]