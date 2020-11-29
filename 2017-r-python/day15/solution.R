# --- Day 15: Dueling Generators ---

# Puzzle input:
# Generator A starts with 699
# Generator B starts with 124


generator_A <- list(value=699, factor=16807)
generator_B <- list(value=124, factor=48271)
generators <- list(generator_A, generator_B)

next_value <- function(generator) return((generator$value * generator$factor) %% 2147483647)
to_bit <- function(value) return(rev(as.numeric(intToBits(value))))
matching <- function(values) return(all(values[[1]][17:32] == values[[2]][17:32]))

match_count <- 0
for (i in 1:40000000) {
    bits <- list()
    for(j in 1:2) {
        value <- next_value(generators[[j]])
        bits[[j]] <- to_bit(value)
        generators[[j]]$value <- value
    }
    if (matching(bits)) match_count <- match_count + 1
}
print(match_count)


# --- Part Two ---

next_satisfying_value <- function(generator, condition) {
    generator$value <- next_value(generator)
    while(generator$value %% condition) generator$value <- next_value(generator)
    return(generator$value)
}

picky_count <- 0
for (i in 1:5000000) {
    generator_A$value <- next_satisfying_value(generator_A, 4)
    generator_B$value <- next_satisfying_value(generator_B, 8)
    if (matching(list(to_bit(generator_A$value), to_bit(generator_B$value)))) picky_count <- picky_count + 1
    
}
print(picky_count)