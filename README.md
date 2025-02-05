# lifeJava

This project will be a basic Java implementation of the Game of Life using Java.

## Purpose

The purpose of this project will be to demonstrate an implementation of the Game of Life using java code so that we can compare and contrast the advantages and disadvantages of each language for this particular task.

## Technical specs

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

### print_generation

A basic funtion that will return a visual print out of the most recently generated cell generation

### print_grid

This optional helper function will be used to create a visual representation of the current state of the grid after each generation runs

### generation_run_speed

This function will be used to optionally return the run speed of each generation

We can use this to get an idea of the run time as we move through generations and potential growth of living cells