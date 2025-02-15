package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LifeHelpersTest {

    // ------------ validateGameInputs --------------

    private Set<Cell> validStartingGeneration;
    private GameConfig validStartingConfig;
    private ByteArrayOutputStream consoleOutput;

    @BeforeEach
    void setUp() {
        validStartingGeneration = Set.of(new Cell(0, 0), new Cell(1, 1));
        validStartingConfig = new GameConfig(true, true, true);

        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
    }

    @Test
    @DisplayName("Validate inputs should not through exceptions")

    void testValidateInputs_NoExceptions() {
        assertDoesNotThrow(() -> LifeHelpers.validateGameInputs(5, validStartingGeneration, validStartingConfig),
                "No exceptions should be throw in valid inputs");
    }

    @Test
    @DisplayName("Validate Generations throws error with invalid generations")

    void testValidateInputs_InvalidGenerations() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(0, validStartingGeneration, validStartingConfig),
                "Exception is throw when generations is 0");

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(-1, validStartingGeneration, validStartingConfig),
                "Exception is thrown when generations is less than 0");
    }

    @Test
    @DisplayName("Validate exception is thrown with null starting generation")

    void testValidateInputs_NullStartingGeneration() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(5, null, validStartingConfig),
                "Exception is thrown when starting generation is null");
    }

    @Test
    @DisplayName("Validate exception is thrown with a null game config")

    void testValidateInputs_NullGameConfig() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(5, validStartingGeneration, null),
                "Exception is thrown when game config is null");
    }

    // ------------ createStartingCellSet --------------

    @Test
    @DisplayName("Test valid coordinates creates a set of cells")

    void testCreateStartingCellSet_ValidCoordinates() {
        long[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };

        Set<Cell> created = LifeHelpers.createStartingCellSet(input);

        Set<Cell> expected = Set.of(
                new Cell(0, 1), new Cell(1, 1),
                new Cell(0, 0), new Cell(1, 0));

        assertEquals(created, expected, "The created set should match the expected result");
    }

    @Test
    @DisplayName("Empty input should return an error")

    void testCreateStartingCellSet_EmptySet() {
        long[][] input = {};

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.createStartingCellSet(input),
                "Error: coordinates must include at least one coordinate");
    }

    @Test
    @DisplayName("Null imput should return an error")

    void testCreateStartingCellSet_Null() {
        long[][] input = null;

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.createStartingCellSet(input),
                "Error: coordinates must include at least one coordinate");
    }

    @Test
    @DisplayName("Malformed coordinates should be ignored")

    void testCreateStartingCellSet_MalformedInput() {
        long[][] input = { { 0, 0 }, { 1 }, { 1, 0 } };

        Set<Cell> result = LifeHelpers.createStartingCellSet(input);

        Set<Cell> expected = Set.of(
                new Cell(0, 0), new Cell(1, 0));

        assertEquals(result, expected, "Malformed inputs should be ignored");
    }

    // ------------ getUserConfigInput --------------

    @Test
    @DisplayName("Test basic acceptable responses for user configs selections")

    void testGetUserConfigInputs_HappyPathValues() {
        Scanner scanner = new Scanner(System.in);
    }

}
