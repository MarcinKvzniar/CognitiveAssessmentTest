package com.example.cognitiveassessmenttest.sudoku

/**
 * This class is responsible for generating and validating Sudoku puzzles.
 */
class SudokuGenerator {

    /**
     * This function checks if a given number can be placed at a given position in the Sudoku board.
     * It checks the row, column, and 2x2 grid for the same number.
     *
     * @param board The Sudoku board represented as a 2D array.
     * @param row The row index where the number is to be placed.
     * @param col The column index where the number is to be placed.
     * @param num The number to be placed.
     * @return Returns true if the number can be placed at the given position, false otherwise.
     */
    fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        if (num < 1 || num > 4) {
            return false
        }
        for (i in 0 until 4) {
            if ((board[row][i] == num && i != col) || (board[i][col] == num && i != row)) {
                return false
            }
            if (board[row / 2 * 2 + i / 2][col / 2 * 2 + i % 2] == num
                && (row / 2 * 2 + i / 2 != row || col / 2 * 2 + i % 2 != col)) {
                return false
            }
        }
        return true
    }
}