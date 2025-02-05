import java.util.HashSet;
import java.util.Set;

public class GameOfLife {
    // Set of cells
    private Set<Cell> liveCells = new HashSet<>();

    // Method to add a cell to a set of cells
    public void addCell(int x, int y) {
        liveCells.add(new Cell(x, y));
    }

    public static void main(String[] args) {

    }
}
