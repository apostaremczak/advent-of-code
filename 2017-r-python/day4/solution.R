# --- Day 4: High-Entropy Passphrases ---

library(sets)

puzzle_input <- read.csv("input.txt", sep = " ", header = FALSE, col.names = 1:11)
# Fill empty cells with NAs
puzzle_input[which(puzzle_input == "", TRUE)] <- NA


# Part 1: a valid passphrase must contain no duplicate words.
row_to_set <- function(row) return(as.set(unlist(row[which(!is.na(row))])))

is_original <- function(row) {
    return(length(row_to_set(row)) == sum(!is.na(row)))
}

count_original <- sum(by(puzzle_input, 1:nrow(puzzle_input), is_original))


# --- Part Two ---

# Now, a valid passphrase must contain no two words that are anagrams of each other - all letters need to be used!

is_anagram <- function(word1, word2) {
    letters1 <- unlist(strsplit(as.character(word1), ""))
    letters2 <- unlist(strsplit(as.character(word2), ""))
    if (!length(letters1) == length(letters2)) return(FALSE)
    for (letter in letters1) {
        if (length(which(letters1 == letter)) != length(which(letters2 == letter))) return(FALSE)
    }
    return(TRUE)
}

is_valid <- function(row) {
    if (!is_not_duplicate(row)) return(FALSE)
    row_set <- row_to_set(row)
    for (word1 in row_set) {
        for (word2 in (row_set - as.set(word1))) {
            if (is_anagram(word1, word2)) return(FALSE)
        }
    }
    return(TRUE)
}

count_valid <- sum(by(puzzle_input, 1:nrow(puzzle_input), is_valid))