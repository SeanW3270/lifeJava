package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CellTest {

    @Test
    void testCellConstructor() {
        // Arange
        Cell cell = new Cell(1, 2);

        // Act & Assert
        assertEquals(1, cell.x());
        assertEquals(2, cell.y());
    }

    @Test
    void testCellConstructorInvalidInputs() {

        assertThrows(NumberFormatException.class, () -> {
            new Cell(Integer.parseInt("abc"), 100);
        });

        assertThrows(NumberFormatException.class, () -> {
            new Cell(Integer.parseInt("100"), Integer.parseInt("xyz"));
        });

    }
}
