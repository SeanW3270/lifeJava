package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameOfLifeTest {

    @Test
    @DisplayName("Return the 8 neighbors of a standard cell using getNeighbors")

    void testGetNeighbors_HappyPath() {
        GameOfLife game = new GameOfLife();
        Cell cell = new Cell(5, 5);

        Set<Cell> neighbors = game.getNeighbors(cell);

        assertEquals(8, neighbors.size(),
                "A cell should return 8 neighbors under normal circumstance.");

        // Expect neighbors
        Set<Cell> expectedNeighbors = Set.of(
                new Cell(4, 6), new Cell(5, 6), new Cell(6, 6),
                new Cell(4, 5), new Cell(6, 5),
                new Cell(4, 4), new Cell(5, 4), new Cell(6, 4));

        assertEquals(expectedNeighbors, neighbors,
                "Neighbors should match the values we would expect when created by 'getNeighbors'.");
    }

    @Test
    @DisplayName("Min value wrapping and out of bounds test")

    void testGetNeighbors_EdgeCase_MinValue() {
        GameOfLife game = new GameOfLife();
        Cell cell = new Cell(Long.MIN_VALUE, Long.MIN_VALUE);

        Set<Cell> neighbors = game.getNeighbors(cell);

        assertFalse(neighbors.contains(new Cell(Long.MIN_VALUE - 1, Long.MIN_VALUE)),
                "Should not include out-of-range neighbor (-1, 0)");
        assertFalse(neighbors.contains(new Cell(Long.MIN_VALUE - 1, Long.MIN_VALUE + 1)),
                "Should not include out-of-range neighbor (-1, +1)");
        assertFalse(neighbors.contains(new Cell(Long.MIN_VALUE - 1, Long.MIN_VALUE - 1)),
                "Should not include out-of-range neighbor (-1, -1)");
        assertFalse(neighbors.contains(new Cell(Long.MIN_VALUE, Long.MIN_VALUE - 1)),
                "Should not include out-of-range neighbor (0, -1)");
        assertFalse(neighbors.contains(new Cell(Long.MIN_VALUE + 1, Long.MIN_VALUE - 1)),
                "Should not include out-of-range neighbor (+1, -1)");
    }

    @Test
    @DisplayName("Max value wrapping and out of bounds test")

    void testGetNeighbors_EdgeCase_MaxValue() {
        GameOfLife game = new GameOfLife();
        Cell cell = new Cell(Long.MAX_VALUE, Long.MAX_VALUE);

        Set<Cell> neighbors = game.getNeighbors(cell);

        assertFalse(neighbors.contains(new Cell(Long.MAX_VALUE + 1, Long.MAX_VALUE)),
                "Should not include out-of-range neighbor (+1, 0)");
        assertFalse(neighbors.contains(new Cell(Long.MAX_VALUE + 1, Long.MAX_VALUE + 1)),
                "Should not include out-of-range neighbor (+1, +1)");
        assertFalse(neighbors.contains(new Cell(Long.MAX_VALUE - 1, Long.MAX_VALUE + 1)),
                "Should not include out-of-range neighbor (-1, +1)");
        assertFalse(neighbors.contains(new Cell(Long.MAX_VALUE, Long.MAX_VALUE + 1)),
                "Should not include out-of-range neighbor (0, +1)");
        assertFalse(neighbors.contains(new Cell(Long.MAX_VALUE + 1, Long.MAX_VALUE - 1)),
                "Should not include out-of-range neighbor (+1, -1)");
    }

    @Test
    @DisplayName("Ensure the number of neighbors returned is 8")

    void testGetNeighbors_NoDuplicates() {
        GameOfLife game = new GameOfLife();
        Cell cell = new Cell(10, 10);

        Set<Cell> neighbors = game.getNeighbors(cell);

        assertEquals(8, neighbors.size(),
                "The method should return 8 unique neighbors.");
    }

}
