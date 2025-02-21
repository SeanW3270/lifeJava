package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

class FullLoopTests {

    @Test
    @DisplayName("Full loop test generating test data and verifying output coordinates")

    void gameOfLifeFullLoopTest_StandardCellInput_GameSucceeds() throws IOException {

        Path exampleFile = Paths.get("src/test/resources/examples/square.txt");

        // Read the input file using existing method
        Set<Cell> intialGeneration = LifeHelpers.getCellsFromFile(exampleFile.toString());
        assertNotNull(intialGeneration, "Failed to read from test file.");

        // Run the game for 10 generations using mock data
        GameOfLife game = new GameOfLife();
        GameConfig config = new GameConfig(false, false, false);
        Set<Cell> resultingGeneration = game.runGenerations(10, intialGeneration, config);

        // Validate expected results
        assertFalse(resultingGeneration.isEmpty(), "Cells should not be empty after 10 generations.");
        assertTrue(resultingGeneration.contains(new Cell(3, 0)), "Expected Cell missing from result");
        assertTrue(resultingGeneration.contains(new Cell(-3, 0)), "Expected Cell missing from result");
        assertTrue(resultingGeneration.contains(new Cell(0, 3)), "Expected Cell missing from result");
        assertTrue(resultingGeneration.contains(new Cell(1, 3)), "Expected Cell missing from result");

        assertFalse(resultingGeneration.contains(new Cell(0, 0)), "Expected absent cell was found in result");
    }

    @Test
    @DisplayName("Full loop test for testing a value set containing cells that are out of bounds")

    void gameOfLifeFullLoopTest_OutOfBoundsCells_CellsAreNotIncludedInResult() throws IOException {

        Set<Cell> initialGeneration = new HashSet<>();

        // Add Cells at the edge of range for Long
        initialGeneration.add(new Cell(Long.MAX_VALUE - 1, 0));
        initialGeneration.add(new Cell(Long.MAX_VALUE - 1, 1));
        initialGeneration.add(new Cell(Long.MAX_VALUE, 0));
        initialGeneration.add(new Cell(Long.MAX_VALUE, 1));

        GameOfLife game = new GameOfLife();
        GameConfig config = new GameConfig(false, false, false);

        // Run a single generation
        Set<Cell> resultingGeneration = game.runGenerations(1, initialGeneration, config);

        // Ensure no new cells appear beyond Long.MAX_VALUE
        assertFalse(resultingGeneration.contains(new Cell(Long.MAX_VALUE + 1, 0)), "Out-of-bounds cell should not exist.");
        assertFalse(resultingGeneration.contains(new Cell(Long.MAX_VALUE + 1, 1)), "Out-of-bounds cell should not exist.");
    
        // Ensure in-bounds cells still evolve correctly
        assertTrue(resultingGeneration.contains(new Cell(Long.MAX_VALUE - 1, 0)), "Expected cell missing from result.");
        assertTrue(resultingGeneration.contains(new Cell(Long.MAX_VALUE - 1, 1)), "Expected cell missing from result.");
    }

    @Test
    @DisplayName("Test to ensure no wrapping occurs when cells reach max bounds")

    void gameOfLifeFullLoopTest_NoWrappingOccurs_WrapCellsNotPresent() throws IOException {
        Set<Cell> initialGeneration = new HashSet<>();

        // Create a small pattern at the upper boundary
        initialGeneration.add(new Cell(Long.MAX_VALUE, 0));
        initialGeneration.add(new Cell(Long.MAX_VALUE, 1));
        initialGeneration.add(new Cell(Long.MAX_VALUE - 1, 0));
        initialGeneration.add(new Cell(Long.MAX_VALUE - 1, 1));

        GameOfLife game = new GameOfLife();
        GameConfig config = new GameConfig(false, false, false);

        Set<Cell> resultingGeneration = game.runGenerations(1, initialGeneration, config);

        // Ensure no cell has incorrectly wrapped to Long.MIN_VALUE
        for (Cell cell : resultingGeneration) {
            assertTrue(cell.x() >= Long.MAX_VALUE - 1, "Cell incorrectly wrapped around in X direction!");
            assertTrue(cell.y() >= Long.MIN_VALUE + 10, "Cell incorrectly wrapped around in Y direction!");
        }
    }

    @Test
    @DisplayName("Load test with large-scale input to measure performance and stability")

    void gameOfLifeFullLoopTest_LargeInput_PerformanceCheck() throws IOException {
        Set<Cell> initialGeneration = new HashSet<>();
        Random random = new Random();

        // Generate 10,000 random starting cells within a reasonable range
        for (int i = 0; i < 10_000; i++) {
            long x = random.nextInt(1000) - 500; // Keep within a moderate range
            long y = random.nextInt(1000) - 500;
            initialGeneration.add(new Cell(x, y));
        }

        GameOfLife game = new GameOfLife();
        GameConfig config = new GameConfig(false, false, false);

        long startTime = System.currentTimeMillis();
        Set<Cell> resultingGeneration = game.runGenerations(1000, initialGeneration, config);
        long endTime = System.currentTimeMillis();

        // Ensure the game completes within 10 seconds (arbitrary soft limit)
        long duration = endTime - startTime;
        assertTrue(duration < 10_000, "Simulation took too long!");

        // Ensure not all cells die (some should survive)
        assertFalse(resultingGeneration.isEmpty(), "All cells died, which is unlikely for 10,000 cells!");
    }
}
