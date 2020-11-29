# --- part 1 ---

input <- read.csv("input.csv", header = FALSE, sep = "\t")

spreadsheet_checksum <- function(spreadsheet) {
    return(sum(by(spreadsheet, 1:nrow(spreadsheet), function(row) max(row) - min(row))))
}

spreadsheet_checksum(input)


# --- part 2 ---

test_input <- data.frame(c(5, 9, 3), c(9, 4, 8), c(2, 7, 6), c(8, 3, 5))

divisible_checksum <- function(spreadsheet) {
    quotient <- function(row) {
        for(x in row) {
            y <- row[!(row %% x) & row != x]
            if (length(y)) return(y/x)
        }
    }
    return(sum(by(spreadsheet, 1:nrow(spreadsheet), quotient)))
}

divisible_checksum(test_input)
divisible_checksum(input)
