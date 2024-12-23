package pl.apostaremczak.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends PuzzleSolution {
    // {{tc, kh}, {tc, qc}, ... }
    private final Set<Set<String>> AllComputerConnections = new HashSet<>();
    // tc -> { {tc, kh}, {tc, qc} }
    private final Map<String, Set<Set<String>>> ComputerConnectionMap = new HashMap<>();

    public Day23(String inputFilename) {
        super(inputFilename);
        for (String line : inputLines) {
            List<String> computers = List.of(line.split("-"));
            Set<String> connection = new HashSet<>(computers);
            AllComputerConnections.add(connection);
            String first = computers.getFirst();
            String second = computers.getLast();
            ComputerConnectionMap.merge(first, new HashSet<>(Set.of(connection)), (existingSet, newSet) -> {
                existingSet.addAll(newSet);
                return existingSet;
            });

            ComputerConnectionMap.merge(second, new HashSet<>(Set.of(connection)), (existingSet, newSet) -> {
                existingSet.addAll(newSet);
                return existingSet;
            });
        }
    }

    @Override
    public Long solvePart1() {
        Set<String> tComputers = ComputerConnectionMap.keySet().stream().filter(k -> k.startsWith("t")).collect(Collectors.toSet());
        Set<Set<String>> TripleLanPartiesWithT = new HashSet<>();

        // Awful nested loops, but it runs in 13 ms on this small input
        for (String tComputer : tComputers) {
            for (Set<String> connection : ComputerConnectionMap.get(tComputer)) {
                String secondComputer = connection.stream().filter(c -> !c.equals(tComputer)).toList().getFirst();
                for (Set<String> secondCompConnection : ComputerConnectionMap.get(secondComputer)) {
                    String thirdComputer = secondCompConnection.stream().filter(c -> !c.equals(secondComputer)).toList().getFirst();
                    if (AllComputerConnections.contains(new HashSet<>(List.of(tComputer, thirdComputer)))) {
                        TripleLanPartiesWithT.add(new HashSet<>(List.of(tComputer, secondComputer, thirdComputer)));
                    }
                }
            }
        }
        return (long) TripleLanPartiesWithT.size();
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Clique_problem#Finding_a_single_maximal_clique">...</a>
     * <p>
     * A single maximal clique can be found by a straightforward greedy algorithm.
     * Starting with an arbitrary clique (for instance, any single vertex or even the empty set),
     * grow the current clique one vertex at a time by looping through the graph's remaining vertices.
     * For each vertex v that this loop examines,
     * - add v to the clique if it is adjacent to every vertex that is already in the clique,
     * - and discard v otherwise.
     */
    public Set<String> findMaximalClique(String firstElement) {
        Set<String> clique = new HashSet<>();
        clique.add(firstElement);

        for (var entry : ComputerConnectionMap.entrySet()) {
            String vertexName = entry.getKey();
            Set<Set<String>> vertexConnections = entry.getValue();
            if (!clique.contains(vertexName)) {
                if (clique.stream()
                        .allMatch(cliqueElement ->
                                vertexConnections.contains(Set.of(vertexName, cliqueElement))
                        )
                ) {
                    clique.add(vertexName);
                }
            }
        }

        return clique;
    }

    @Override
    public Long solvePart2() {
        Set<String> lanParty = new HashSet<>();
        // The final result of depends on the first element we choose to include in the clique;
        // Try different starting points
        for (String vertex : ComputerConnectionMap.keySet()) {
            if (!lanParty.contains(vertex)) {
                Set<String> vertexParty = findMaximalClique(vertex);
                if (vertexParty.size() > lanParty.size()) {
                    lanParty = vertexParty;
                }
            }
        }
        String password = String.join(",", lanParty.stream().sorted().toList());
        System.out.println("Party password: " + password);
        return 0L;
    }

    public static void main(String[] args) {
        long startTotal = System.currentTimeMillis();
        Day23 day = new Day23("src/main/resources/23.txt");

        long startPart1 = System.currentTimeMillis();
        Long part1Solution = day.solvePart1();
        long endPart1 = System.currentTimeMillis();
        System.out.println("Part 1: " + part1Solution + " (Time: " + (endPart1 - startPart1) + " ms)");

        long startPart2 = System.currentTimeMillis();
        Long part2Solution = day.solvePart2();
        long endPart2 = System.currentTimeMillis();
        System.out.println("Part 2: " + part2Solution + " (Time: " + (endPart2 - startPart2) + " ms)");

        long endTotal = System.currentTimeMillis();
        System.out.println("Total time: " + (endTotal - startTotal) + " ms");
    }
}
