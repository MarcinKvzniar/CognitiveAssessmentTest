package com.example.cognitiveassessmenttest.sudoku

class SudokuGenerator {

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