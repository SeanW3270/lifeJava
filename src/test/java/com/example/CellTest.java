package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

public class CellTest {

    @Test
    @DisplayName("Testing basic cell constructor functionality")

    void testCellConstructor() {
        // Arange
        Cell cell = new Cell(1L, 2L);

        // Act & Assert
        assertEquals(1L, cell.x());
        assertEquals(2L, cell.y());
    }

    @Test
    @DisplayName("Testing cell creation at extreme values")

    void testCellConstructorExtremeValues() {

        Cell minCell = new Cell(Long.MIN_VALUE, Long.MIN_VALUE);
        Cell maxCell = new Cell(Long.MAX_VALUE, Long.MAX_VALUE);

        assertEquals(Long.MIN_VALUE, minCell.x());
        assertEquals(Long.MIN_VALUE, minCell.y());

        assertEquals(Long.MAX_VALUE, maxCell.x());
        assertEquals(Long.MAX_VALUE, maxCell.y());
    }
}
