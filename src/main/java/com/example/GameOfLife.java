package com.example;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class GameOfLife {
    /**
     * Used to generate a set of neighbor cells on a 2x2 grid
     * 
     * @param cell - An (x, y) tuple of coordinates for a cell
     * @return - Returns a Set of 8 tuple neighboring cells
     */
    public Set<Cell> getNeighbors(Cell cell) {
        Set<Cell> potentialCells = new HashSet<>();

        long[][] coordinates = {

                { -1, 1 }, { 0, 1 }, { 1, 1 }, // Top row of neighbors
                { -1, 0 }, { 1, 0 }, // Middle row of neighbors
                { -1, -1 }, { 0, -1 }, { 1, -1 } // Bottom row of neighbors
        };
        for (long[] cor : coordinates) {

            // Add each value to potential cells as long as they are within acceptable range
            long newX = cell.x() + cor[0];
            long newY = cell.y() + cor[1];

            // Prevent overflow if newX would try to wrap
            if (!((cor[0] > 0 && newX < cell.x()) || (cor[0] < 0 && newX > cell.x())) &&
                    !((cor[1] > 0 && newY < cell.y()) || (cor[1] < 0 && newY > cell.y()))) {
                potentialCells.add(new Cell(newX, newY));
            }
        }

        return potentialCells;
    }

    /**
     * Run a set of living cells through the rules of the game of life
     * 
     * @param aliveCells - The most recent set of living cells for a generation
     * @return - Returns the new set of living cells after applying the rules of the
     *         game
     */
    public Set<Cell> getNextGeneration(Set<Cell> aliveCells) {
        Set<Cell> newLivingCells = new HashSet<>();
        Set<Cell> potentialCells = new HashSet<>();

        // Fill out potential cells with all living cells and neighbors
        for (Cell cell : aliveCells) {
            potentialCells.add(cell); // add a cell
            potentialCells.addAll(getNeighbors(cell)); // add its neighbors
        }

        for (Cell cell : potentialCells) {
            int livingNeighbors = (int) getNeighbors(cell).stream() // Get the number of living neighbors for a cell
                    .filter(aliveCells::contains)
                    .count();
            if (aliveCells.contains(cell)) { // If a cell is alive, apply rule
                if (livingNeighbors == 2 || livingNeighbors == 3) {
                    newLivingCells.add(cell);
                }
            } else { // If the cell is dead, apply rule
                if (livingNeighbors == 3) {
                    newLivingCells.add(cell);
                }
            }
        }
        return newLivingCells;
    }

    /**
     * Run each round of cell generations through the game of life to generate the
     * next generation of the game
     * 
     * @param generations        - How many rounds of the game we want to run
     * @param startingGeneration - The starting set of living cells to run through
     *                           the game
     * @param config             - Included parameter for testing and readout
     *                           information about runs
     */
    public Set<Cell> runGenerations(int generations, Set<Cell> startingGeneration, GameConfig config) {
        LifeHelpers.validateGameInputs(generations, startingGeneration, config);
        Set<Cell> resultingGeneration = new HashSet<>(startingGeneration);
        try {
            for (int runs = 1; runs < generations; runs++) {
                long startTime = System.nanoTime(); // Start timer for game run time
                Set<Cell> newGeneration = getNextGeneration(resultingGeneration);
                resultingGeneration = newGeneration;

                // put our working helper functions here..
                if (config.printCells) {
                    LifeHelpers.printGenerations(resultingGeneration);
                }
                if (config.printGrid) {
                    LifeHelpers.printGenerationGrid(resultingGeneration);
                }
                if (config.runTime) {
                    LifeHelpers.printGameRuntime(startTime);
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while running the Game of Life: " + e.getMessage());
            e.printStackTrace();
        }

        return resultingGeneration;
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Usage: java GameOfLife <filepath>");
            return;
        }
        try {
            Set<Cell> startingCells = LifeHelpers.getCellsFromFile(args[0]);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter number of generations to run: ");
            int generations = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get user configuration
            GameConfig config = LifeHelpers.getUserGameConfig(scanner);
            scanner.close();

            GameOfLife game = new GameOfLife();
            game.runGenerations(generations, startingCells, config);
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
