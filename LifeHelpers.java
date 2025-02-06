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
        long minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        long minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

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
    }

    /**
     * Helper function to return the millisecond run time of a generation of game
     * 
     * @param timerStart - Start time of round of core game loop
     */
    public static void printGameRuntime(long timerStart) {
        long endingTime = System.nanoTime();
        long runDuration = endingTime - timerStart;
        System.out.println("Execution time: " + runDuration / 1_000_000.0 + " milliseconds");
    }
}
