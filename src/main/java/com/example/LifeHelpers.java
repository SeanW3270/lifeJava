package com.example;

import java.util.HashSet;
import java.util.Set;

public class LifeHelpers {
    /**
     * A simple helper function to print a readable output of the cells in a
     * generation by their coordinates on a board
     * 
     * @param cells - A tuple set of cells to be printed in the format ( x, y ) ...
     */
    public static void printGenerations(Set<Cell> cells) {
        for (Cell cell : cells) {
            System.out.print("( " + cell.x() + " ," + cell.y() + " )");
        }
        System.out.println("\n\n");
    }

    /**
     * Helper function to print a robust representation of a grid of cells in two
     * dimensions with X representing living cells
     * 
     * @param cells - A tuple set of cells used to create the board based on x and y
     *              positions
     */
    public static void printGenerationGrid(Set<Cell> cells) {
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("Error: Provided set is empty");
        }
        long minX = Long.MAX_VALUE, maxX = Long.MIN_VALUE;
        long minY = Long.MAX_VALUE, maxY = Long.MIN_VALUE;

        for (Cell cell : cells) {
            minX = Math.min(minX, cell.x());
            maxX = Math.max(maxX, cell.x());
            minY = Math.min(minY, cell.y());
            maxY = Math.max(maxY, cell.y());
        }
        // Print grid
        for (long y = maxY; y >= minY; y--) {
            for (long x = minX; x <= maxX; x++) {
                if (cells.contains(new Cell(x, y))) {
                    System.out.print("X ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    /**
     * Helper function to return the millisecond run time of a generation of game
     * 
     * @param timerStart - Start time of round of core game loop
     */
    public static void printGameRuntime(long timerStart) {
        long endingTime = System.nanoTime();
        long runDuration = endingTime - timerStart;
        System.out.println("Execution time: " + runDuration / 1_000_000.0 + " milliseconds \n");
    }

    /**
     * Helper function to check game inputs before attempting to run generations
     * 
     * @param generations        - Game generation integer
     * @param startingGeneration - Starting set of cells for the game
     * @param config             - Helper function configs
     */
    public static void validateGameInputs(int generations, Set<Cell> startingGeneration, GameConfig config) {
        if (generations <= 0) {
            throw new IllegalArgumentException("Error: Number of generations must be greater than zero");
        }
        if (startingGeneration == null) {
            throw new IllegalArgumentException("Error: Starting generation must not be null or empty");
        }
        if (config == null) {
            throw new IllegalArgumentException("Error: Game config must not be null");
        }
    }

    /**
     * Helper function that creates a set of sells for the game of life from a two
     * dimensional array of coordinates
     * If the supplied array does not meet the requirements of (x, y) coordinates
     * this funtion will return a blank set of cells
     * 
     * @param coordinates - A two dimensional array of (x, y) coordinates
     * @return - Returns a set of cells as (x, y) coordinates
     */
    public static Set<Cell> createStartingCellSet(long[][] coordinates) {

        if (coordinates == null || coordinates.length == 0) {
            throw new IllegalArgumentException("Error: coordinates must include at least one coordinate");
        }

        Set<Cell> startingCellSet = new HashSet<>();
        for (long[] cor : coordinates) {
            if (cor.length == 2) {
                startingCellSet.add(new Cell(cor[0], cor[1]));
            }
        }
        return startingCellSet;
    }
}
