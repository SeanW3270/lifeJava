# lifeJava

This project will be a basic Java implementation of the Game of Life using Java.

## Purpose

The purpose of this project will be to demonstrate an implementation of the Game of Life using java code so that we can compare and contrast the advantages and disadvantages of each language for this particular task.

## Prerequisites

- **Java** (JDK 17 or later)
- **Maven** (Dependency management and testing)
- **JUnit 5** (Unit testing)
- **Mockito** (Unit testing)

> Install Java (JDK 17+)

check initial version with

```sh
java -version
```

### Cloning Repository

To get the code:
```sh
git clone <repository-url>
cd game-of-life
```

### Building the Project

It is recommended to use Maven
```sh
mvn clean compile
```

Using Java compiler
```sh
javac -d target/classes src/main/java/com/examples/*.java
```

### Running the Game

After compiling, run the game with:
```sh
java -cp target/classes com.examples.GameOfLife <path-to-input-file> <number-of-generations>
```
Example:
```sh
java -cp target/classes com.examples.GameOfLife input.txt 10
```
Input files should be formatted in the following way:
```sh
#Life 1.06
0 1
1 2
2 0
2 1
2 2
```

### Configuration options

During execution the game will prompt for the following config values:

> Print cell coordinates (y/n)
This will print cell coordinates after each generation
> Print grid representation (y/n)
This will print a visual representation of the grid in terminal. (Causion using this option with cell sets that are large or may become large)
> Show execution time (y/n)
This option will print the amount of operation time each generation required

Enter `y` or `n` for each prompt

### .gitignore Setup

To prevent compiled files from being committed, ensure your `.gitignore` includes:
```sh
target/
.class
```


## Testing

For testing run the following commands:
```sh
mvn clean install
```

```sh
mvn test
```

## Functional Method Breakdown

### get_neighbors

This function is designed to return all of the living and dead neighbor positions of a given cell inside of the list of input cells

### get_next_generation

This function is designed to run a set of living cells and all neighbor positions through the rules of the Game of Life.

> Living cell with 2 or 3 neighbors lives to the next generation

> Dead cells with 3 neighbors become alive

> Living cells with more than three neighbors become dead

> Living cells with fewer than 2 neighbors becomes dead

### run_generations

This function will be used to invoke the other functions as we walk through each generation

This function will also be used to invoke any helper functions we may create to improve readability or our ability to debug

## Helper Functions

### printGenerations

A basic funtion that will return a visual print out of the most recently generated cell generation

### printGenerationGrid

This optional helper function will be used to create a visual representation of the current state of the grid after each generation runs

### printGameRuntime

This function will be used to optionally return the run speed of each generation

We can use this to get an idea of the run time as we move through generations and potential growth of living cells

### validateGameInputs

Helper function to validate game inputs