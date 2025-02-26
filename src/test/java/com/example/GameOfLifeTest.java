package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;

public class GameOfLifeTest {

    private GameOfLife game;

    @BeforeEach
    void setUpGame() {
        game = new GameOfLife();
    }

    // ------------ getNeighbors --------------

    @Test
    @DisplayName("Return the 8 neighbors of a standard cell using getNeighbors")

    void getNeighbors_StandardCellInput_CellsReturned() {
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

    void getNeighbors_MinOutOfRangeInputs_ReturnsFalse() {
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

    void getNeighbors_MaxOutOfRangeInputs_ReturnsFalse() {
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

    void getNeighbors_StandardCellNeighbors_ReturnsTrueForNeighborCount() {
        Cell cell = new Cell(10, 10);

        Set<Cell> neighbors = game.getNeighbors(cell);

        assertEquals(8, neighbors.size(),
                "The method should return 8 unique neighbors.");
    }

    // ------------ getNextGeneration --------------

    @Test
    @DisplayName("Single cell should die due to underpopulation")

    void getNextGeneration_SingleCellInput_CellDies() {
        Set<Cell> initialState = Set.of(new Cell(0, 0));

        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertTrue(nextGen.isEmpty(), "A single cell should die due to underpopulation");
    }

    @Test
    @DisplayName("Stable cell structure should remain stable")

    void getNextGeneration_StableCellStructure_RemainsStable() {
        Set<Cell> initialState = Set.of(
                new Cell(0, 0), new Cell(1, 0),
                new Cell(0, -1), new Cell(1, -1));
        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertEquals(initialState, nextGen, "A 2x2 block of cells should remain stable after a round of the game");
    }

    @Test
    @DisplayName("Oscillation should occur with a line of three cells in a row")

    void getNextGeneration_OscillationCellStructure_OscillationOccurs() {
        Set<Cell> initialState = Set.of(
                new Cell(0, 0), new Cell(1, 0), new Cell(2, 0));
        Set<Cell> oscilation = Set.of(
                new Cell(1, 1),
                new Cell(1, 0),
                new Cell(1, -1));

        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertEquals(oscilation, nextGen, "A line of three cells oscilates between horizontal and vertical");
    }

    @Test
    @DisplayName("Reproduction should occur when rules are satisfied")

    void getNextGeneration_CellSetToReproduce_CellsReproduce() {
        Set<Cell> initialState = Set.of(
                new Cell(1, 1), new Cell(2, 1),
                new Cell(2, 0));
        Set<Cell> newCell = Set.of(

                new Cell(1, 0));
        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertTrue(nextGen.containsAll(newCell), "A new cell should appear at (1, 0) after one round of the game");
    }

    @Test
    @DisplayName("Overcrowding should cause a cell to die")

    void getNextGeneration_OvercrowdedCells_CellsDie() {
        Set<Cell> initialState = Set.of(
                new Cell(0, 1),
                new Cell(-1, 0), new Cell(0, 0), new Cell(1, 0),
                new Cell(0, -1));
        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertFalse(nextGen.contains(new Cell(0, 0)), "Cell (0, 0) dies during the run due to overcrowding");
    }

    @Test
    @DisplayName("An Empty grid of cells should remain empty")

    void getNextGeneration_EmptySetOfCells_CellGridRemainsEmpty() {
        Set<Cell> initialState = Set.of();

        Set<Cell> nextGen = game.getNextGeneration(initialState);

        assertTrue(nextGen.isEmpty(), "An empty grid of cells should remain empty");
    }

    // ------------ runGenerations --------------

    @Test
    @DisplayName("runGenerations should run as many times as it is told through the generations value")

    void runGenerations_StandardNumberOfGenerations_GenerationsRun() {
        Set<Cell> intialState = Set.of(new Cell(0, 0), new Cell(1, 0), new Cell(2, 0));
        GameConfig config = new GameConfig(false, false, false);

        GameOfLife game = spy(new GameOfLife()); // Spy on actual game of life instance

        game.runGenerations(5, intialState, config);

        verify(game, times(4)).getNextGeneration(anySet());
    }

    @Test
    @DisplayName("Verify that validateGameInputs runs one time for a game run")

    void runGenerations_VerifyValidateGameInputs_ValidateGameInputsRuns() {
        Set<Cell> intialState = Set.of(new Cell(0, 0));
        GameConfig config = new GameConfig(false, false, false);

        try (MockedStatic<LifeHelpers> mockedHelpers = mockStatic(LifeHelpers.class)) {
            GameOfLife game = new GameOfLife();
            game.runGenerations(3, intialState, config);

            mockedHelpers.verify(() -> LifeHelpers.validateGameInputs(3, intialState, config), times(1));
        }
    }

    @Test
    @DisplayName("Ensure that helper methods are being called based on config settings")

    void runGenerations_TrueGameConfig_ConfigHelpersAreRun() {
        Set<Cell> initialState = Set.of(new Cell(0, 0), new Cell(1, 0), new Cell(2, 0));
        GameConfig config = new GameConfig(true, true, true);

        try (MockedStatic<LifeHelpers> mockedHelpers = mockStatic(LifeHelpers.class)) {
            game.runGenerations(2, initialState, config);

            mockedHelpers.verify(() -> LifeHelpers.printGenerations(anySet()), atLeastOnce());
            mockedHelpers.verify(() -> LifeHelpers.printGenerationGrid(anySet()), atLeastOnce());
            mockedHelpers.verify(() -> LifeHelpers.printGameRuntime(anyLong()), atLeastOnce());
        }
    }

    @Test
    @DisplayName("Error handling for generations equal to zero")

    void runGenerations_GenerationsSetToZero_ThrowsException() {
        Set<Cell> intialState = Set.of(new Cell(0, 0));
        GameConfig config = new GameConfig(false, false, false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            game.runGenerations(0, intialState, config);
        });

        assertEquals("Error: Number of generations must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Ensure exception handling is working")

    void runGenerations_ThrowTestException_ThrowsException() {
        Set<Cell> intialState = Set.of(new Cell(0, 0));
        GameConfig config = new GameConfig(false, false, false);

        GameOfLife game = spy(new GameOfLife());

        // Throw and exception
        doThrow(new RuntimeException("Test Exception")).when(game).getNextGeneration(anySet());

        assertDoesNotThrow(() -> game.runGenerations(3, intialState, config), "Exception should not cause a crash");
    }
}
