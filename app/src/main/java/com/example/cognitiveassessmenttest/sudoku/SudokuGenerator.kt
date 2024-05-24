package com.example.cognitiveassessmenttest.sudoku

class SudokuGenerator {

    fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 4) {
            if (board[row][i] == num || board[i][col] == num) {
                return false
            }
            if (board[row / 2 * 2 + i / 2][col / 2 * 2 + i % 2] == num) {
                return false
            }
        }
        return true
    }

}