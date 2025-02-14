package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import java.util.Set;

class FullLoopTests {

    @Test
    @DisplayName("Full loop test generating test data and verifying output coordinates")

    void gameOfLifeFullLoopTest() throws IOException {

        // Generate a temp test file with an established pattern
        Path tempFile = Files.createTempFile("test_life", ".txt");
        Files.writeString(tempFile, """
                #Life 1.06
                -1 1
                0 1
                1 1
                -1 0
                0 0
                1 0
                -1 -1
                0 -1
                1 -1
                """);

        // Read the input file using existing method
        Set<Cell> intialGeneration = LifeHelpers.getCellsFromFile(tempFile.toString());
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

        // Clean up mock data
        Files.delete(tempFile);
    }
}