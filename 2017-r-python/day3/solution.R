# --- Day 3: Spiral Memory ---

test_inputs <- c(1, 12, 23, 1024)
puzzle_input <- 347991

# Determine minimal n so that the x field is contained in a n x n spiral matrix
min_n <- function(x) {
    n <- 1
    while (!x <= n^2) n <- n + 2
    return(n)
}

# Is (x, y) inside a m x m square with centre in the middle of the plane?
is_inside <- function(centre, x, y, m) {
    allowed <- floor(m/2)
    return(abs(x - centre) <= allowed & abs(y - centre) <= allowed)
}

get_next_move <- function(plane, x, y, m, dir, centre) {
    # While (x, y) is inside, go in this direction
    if (dir == "R") {
        y <- y + 1
        if (!is_inside(centre, x, y, m)) {
            dir <- "U"
            m <- m + 2
        }
    }
    else if (dir == "U") {
        x <- x - 1
        if (!is_inside(centre, x - 1, y,  m)) {
            dir <- "L"
        }
    }
    else if (dir == "L") {
        y <- y - 1
        if (!is_inside(centre, x, y - 1, m)) {
            dir <- "D"
        }
    }
    else if (dir == "D") {
        x <- x + 1
        if (!is_inside(centre, x + 1, y, m)) {
            dir <- "R"
        }
    }
    
    return(list(x, y, m, dir))
}


# Create a spiral plane up to a given number field
create_plane <- function(field) {
    n <- min_n(field)
    # Generate n x n matrix to be filled with numbers
    plane <- matrix(numeric(n^2), n, n, byrow = TRUE)
    
    # Start at the centre of the plane
    x <- y <- centre <- median(seq_len(n))
    dir <- "R"
    m <- 1

    for(i in 1:n^2) {
        plane[x, y] <- i
        next_move <- get_next_move(plane, x, y, m, dir, centre)
        x <- next_move[[1]]
        y <- next_move[[2]]
        m <- next_move[[3]]
        dir <- next_move[[4]]
    }

    return(plane)
}



# Calculate the Manhattan distance between two points
manhattan_distance <- function(p, q) {
    return(abs(p[1] - q[1]) + abs(p[2] - q[2]))
}


# How many steps are required to carry the data from the square identified 
# in a puzzle input all the way to the access port?
how_many_steps <- function(field) {
    plane <- create_plane(field)
    start_coord <- which(plane == 1, TRUE)
    point_coord <- which(plane == field, TRUE)
    
    return(manhattan_distance(start_coord, point_coord))
}


for(test_input in test_inputs) {
    print(how_many_steps(test_input))
}

how_many_steps(puzzle_input)



# --- Part Two ---

adj_sum <- function(plane, x, y) {
    n <- nrow(plane)
    res <- 0
    if (x > 1) {
        res <- res + plane[x - 1, y]
        if (y > 1) res <- res + plane[x - 1, y - 1]
        if (y < n) res <- res + plane[x - 1, y + 1]
    }
    if (x < n) {
        res <- res + plane[x + 1, y]
        if (y < n) res <- res + plane[x + 1, y + 1]
        if (y > 1) res <- res + plane[x + 1, y - 1]
    }
    if (y > 1) res <- res + plane[x, y - 1]
    if (y < n) res <- res + plane[x, y + 1]

    return(res)
}


first_greater <- function(field) {
    n <- min_n(field)
    # Generate n x n matrix to be filled with numbers
    plane <- matrix(numeric(n^2), n, n, byrow = TRUE)
    
    # Start at the centre of the plane
    x <- y <- centre <- median(seq_len(n))
    dir <- "R"
    m <- 1
    
    for(i in 1:n^2) {
        if (x == centre & y == centre) plane[x, y] <- i
        else plane[x, y] <- adj_sum(plane, x, y)
        if (plane[x, y] > field){
                return(plane[x, y])
        }
        next_move <- get_next_move(plane, x, y, m, dir, centre)
        x <- next_move[[1]]
        y <- next_move[[2]]
        m <- next_move[[3]]
        dir <- next_move[[4]]
    }
    
    return(plane)
}

first_greater(puzzle_input)