LifeJava

A Java-based implementation of Conway's Game of Life, demonstrating key principles of cellular automata, algorithmic efficiency, and software design.

📌 Project Overview

This project provides an interactive and configurable simulation of the Game of Life, allowing users to specify initial conditions and customize execution parameters.

📋 Prerequisites

Ensure you have the following installed before running the project:

Java (JDK 17 or later)

Maven (for dependency management and testing)

JUnit 5 (for unit testing)

Mockito (for unit test mocking)

Check your Java installation with:

java -version

📥 Cloning the Repository

Clone the repository using:

git clone <repository-url>
cd lifeJava

🛠️ Building the Project

Using Maven (Recommended)

mvn clean compile

Using Java Compiler

javac -d target/classes src/main/java/com/example/*.java

🚀 Running the Simulation

After compiling, run the game with:

java -cp target/classes com.example.GameOfLife <path-to-input-file>

Example:

java -cp target/classes com.example.GameOfLife examples/test_data.txt

📝 Input File Format

Input files should be formatted in Life 1.06 format:

#Life 1.06
0 1
1 2
2 0
2 1
2 2

Each line represents a living cell with its x and y coordinates.

⚙️ Configuration Options

During execution, you will be prompted to configure:

Print cell coordinates - Display living cell positions after each generation.

Print grid representation - Show a visual representation of the simulation grid.

Show execution time - Display the runtime for each generation.

Enter y (yes) or n (no) when prompted.

🧪 Running Tests

Run tests using Maven:

mvn test

Or build and test:

mvn clean install

🏗️ Functional Breakdown

🔹 getNeighbors(Cell cell)

Returns all living and dead neighboring positions of a given cell.

🔹 getNextGeneration(Set<Cell> currentGeneration)

Computes the next state of the grid based on:

A living cell with 2 or 3 neighbors survives.

A dead cell with exactly 3 neighbors becomes alive.

A living cell with fewer than 2 or more than 3 neighbors dies.

🔹 runGenerations(int generations, Set<Cell> startingGeneration, GameConfig config)

Iterates through multiple generations.

Calls helper functions for debugging and visualization.

🛠️ Helper Functions

✅ printGenerations(Set<Cell> cells)

Prints a textual representation of the current generation.

✅ printGenerationGrid(Set<Cell> cells)

Displays a structured grid of the current generation.

✅ printGameRuntime(long startTime)

Calculates and prints execution time per generation.

✅ validateGameInputs(int generations, Set<Cell> startingGeneration, GameConfig config)

Ensures that input values are valid before running the simulation.

📂 .gitignore Setup

Ensure compiled files and build artifacts are excluded from Git commits:

target/
*.class