package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    // ------------ validateGameInputs --------------

    @Test
    @DisplayName("Validate inputs should not throw exceptions")

    void validateGameInputs_NormalInputs_NoExceptions() {
        assertDoesNotThrow(() -> LifeHelpers.validateGameInputs(5, validStartingGeneration, validStartingConfig),
                "No exceptions should be throw in valid inputs");
    }

    @Test
    @DisplayName("Validate Generations throws error with invalid generations")

    void validateGameInputs_InvalidGenerations_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(0, validStartingGeneration, validStartingConfig),
                "Exception is throw when generations is 0");

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(-1, validStartingGeneration, validStartingConfig),
                "Exception is thrown when generations is less than 0");
    }

    @Test
    @DisplayName("Validate exception is thrown with null starting generation")

    void validateGameInputs_NullGeneration_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(5, null, validStartingConfig),
                "Exception is thrown when starting generation is null");
    }

    @Test
    @DisplayName("Validate exception is thrown with a null game config")

    void validateGameInputs_NullGameConfig_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.validateGameInputs(5, validStartingGeneration, null),
                "Exception is thrown when game config is null");
    }

    // ------------ createStartingCellSet --------------

    @Test
    @DisplayName("Test valid coordinates creates a set of cells")

    void createStartingCells_ValidDataInput_SuccessfullyCreated() {
        long[][] input = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };

        Set<Cell> created = LifeHelpers.createStartingCellSet(input);

        Set<Cell> expected = Set.of(
                new Cell(0, 1), new Cell(1, 1),
                new Cell(0, 0), new Cell(1, 0));

        assertEquals(created, expected, "The created set should match the expected result");
    }

    @Test
    @DisplayName("Empty input should return an error")

    void createStartingCells_EmptyInput_ThrowsException() {
        long[][] input = {};

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.createStartingCellSet(input),
                "Error: coordinates must include at least one coordinate");
    }

    @Test
    @DisplayName("Null imput should return an error")

    void createStartingCells_NullInput_ThrowsException() {
        long[][] input = null;

        assertThrows(IllegalArgumentException.class,
                () -> LifeHelpers.createStartingCellSet(input),
                "Error: coordinates must include at least one coordinate");
    }

    @Test
    @DisplayName("Malformed coordinates should be ignored")

    void createStartingCells_MalformedInputs_InputsIgnored() {
        long[][] input = { { 0, 0 }, { 1 }, { 1, 0 } };

        Set<Cell> result = LifeHelpers.createStartingCellSet(input);

        Set<Cell> expected = Set.of(
                new Cell(0, 0), new Cell(1, 0));

        assertEquals(result, expected, "Malformed inputs should be ignored");
    }

    // ------------ getUserConfigInput --------------

    @Test
    @DisplayName("Test to enter 'y' in the config input")

    void getUserConfigInput_YesInputs_ReturnsTrue() {
        
    ByteArrayInputStream inputStream = new ByteArrayInputStream("y\n".getBytes());
    Scanner scanner = new Scanner(inputStream);

    boolean result = LifeHelpers.getUserConfigInput(scanner);
    assertTrue(result, "Expected 'y' to return true");
    }

    @Test
    @DisplayName("Test to enter 'n' in the config input")

    void getUserConfigInput_NoInputs_ReturnsFalse() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("n\n".getBytes());
        Scanner scanner = new Scanner(inputStream);

        boolean result = LifeHelpers.getUserConfigInput(scanner);
        assertFalse(result, "Expected 'n' to return false");
    }

    @Test
    @DisplayName("Test entering first and invalid response then a valid response")

    void getUserConfigInput_InvalidThenValid_ReturnsFalse() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid\nn\ny\n".getBytes());
        Scanner scanner = new Scanner(inputStream);

        boolean result = LifeHelpers.getUserConfigInput(scanner);
        assertFalse(result, "Expected 'n' after invalid input to return false");
    }

    // ------------ getCellsFromFile --------------

    @Test
    @DisplayName("Test adding cells with a valid 1.06 format to a Cell object")

    void getCellsFromFile_ValidTestFile_Success() throws IOException {
        Path tempFile = Files.createTempFile("valid_life", ".txt");
        Files.writeString(tempFile, """
                #Life 1.06
                0 0
                1 1
                -2 -3
                """);

        Set<Cell> cells = LifeHelpers.getCellsFromFile(tempFile.toString());
        assertNotNull(cells, "Set of cells should not be null.");
        assertEquals(3, cells.size(), "Expected to contain 3 cells.");
        assertTrue(cells.contains(new Cell(0, 0)), "Cell (0,0) should be included in the set.");
        assertTrue(cells.contains(new Cell(1, 1)), "Cell (1,1) should be included in the set.");
        assertTrue(cells.contains(new Cell(-2, -3)), "Cell (-2,-3) should be included in the set.");

        Files.delete(tempFile);
    }

    @Test
    @DisplayName("Test getting cells from a file missing a header")

    void getCellsFromFile_FileMissingHeader_ThrowException() throws IOException {
        Path tempFile = Files.createTempFile("missing_header", ".txt");
        Files.writeString(tempFile, """
                0 0
                1 1
                """);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            LifeHelpers.getCellsFromFile(tempFile.toString())
            );

            assertTrue(exception.getMessage().contains("Invalid file format"), "Expected invalid format exception.");

            Files.delete(tempFile);
    }

    @Test
    @DisplayName("Test including invalid data in file set")

    void getCellsFromFile_IncludeInvalidData_CreateSuccessfully() throws IOException {
        Path tempFile = Files.createTempFile("contains_invalid_coords", ".txt");
        Files.writeString(tempFile, """
                #Life 1.06
                0 0
                invalid data
                3 x
                2 2
                """);

        Set<Cell> cells = LifeHelpers.getCellsFromFile(tempFile.toString());
        assertNotNull(cells, "The set should create and not be null.");
        assertEquals(2, cells.size(), "Only two valid cells should be found in the set.");
        assertTrue(cells.contains(new Cell(0, 0)), "The valid Cell (0,0) should be found in the set.");
        assertTrue(cells.contains(new Cell(2, 2)), "The valid Cell (2,2) should be found in the set.");

        Files.delete(tempFile);
    }

    @Test
    @DisplayName("Test getting cells from a file containing a comment inside of it")

    void getCellsFromFile_FileContainsComment_CreateSuccessfully() throws IOException {
        Path tempFile = Files.createTempFile("contains_comments", ".txt");
        Files.writeString(tempFile, """
                #Life 1.06

                # This is a comment
                1 1
                # Another comment
                2 2
                """);

        Set<Cell> cells = LifeHelpers.getCellsFromFile(tempFile.toString());
        assertNotNull(cells, "The set should create and not be null.");
        assertEquals(2, cells.size(), "Only two valid cells should be included in the created set.");
        assertTrue(cells.contains(new Cell(1, 1)), "The valid Cell (1,1) should be found in the set.");
        assertTrue(cells.contains(new Cell(2, 2)), "The valid Cell (2,2) should be found in the set.");

        Files.delete(tempFile);
    }

    @Test
    @DisplayName("Test creating a set of cells from an empty file")

    void getCellsFromFile_EmptyFile_ErrorIsThrown() throws IOException {
        Path tempFile = Files.createTempFile("empty_file", ".txt");
        Files.writeString(tempFile, "");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            LifeHelpers.getCellsFromFile(tempFile.toString())
        );

        assertTrue(exception.getMessage().contains("Invalid file format"), "Expected invalid file format exception.");

        Files.delete(tempFile);
    }
}
