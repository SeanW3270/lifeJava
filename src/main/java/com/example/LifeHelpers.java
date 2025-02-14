package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
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

    /**
     * Helper function that will read in a file of values for the game of life in
     * #Life 1.06 format
     * and convert it into a set of cells to be used for the game
     * 
     * @param filePath - A filepath to a document containing cell coordinates
     * @return - Returns a Set<Cell> that can then be used as a starting generation
     *         of cells
     * @throws FileNotFoundException
     */
    public static Set<Cell> getCellsFromFile(String filePath) throws FileNotFoundException {
        Set<Cell> startingCells = new HashSet<>();
        Scanner scanner = new Scanner(new File(filePath));

        if (!scanner.hasNextLine() || !scanner.nextLine().trim().equals("#Life 1.06")) {
            scanner.close();
            throw new IllegalArgumentException("Invalid file format. Expected '#Life 1.06' as first line.");
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length == 2) {
                try {
                    long x = Long.parseLong(parts[0]);
                    long y = Long.parseLong(parts[1]);
                    startingCells.add(new Cell(x, y));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid coordinates: " + line);
                }
            } else {
                System.err.println("Ignoring invalid line: " + line);
            }
        }
        scanner.close();
        return startingCells;
    }

    /**
     * A simple method that will read in user inputs for each of the game config
     * values
     * 
     * @param scanner - Utilizes an existing scanner to read inputs from command
     *                line for each value response
     * @return - Returns boolean values for each config value
     */
    public static boolean getUserConfigInput(Scanner scanner) {
        String response;
        while (true) {
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            }
        }
    }

}
