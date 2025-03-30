package com.app.zuludin.slidecontentpuzzle.core.utils

class GameEngine(private var size: Int = 3) {
    fun scrambleBoard(board: List<Int>): List<Int> {
        val inversion = countInversion(board)

        if (size % 2 == 1) {
            if (inversion % 2 == 0) return board
            else scrambleBoard(board.shuffled())
        } else {
            val index = board.indexOf(0)
            val blank = findBlankPosition(index)
            return if ((blank % 2 == 0 && inversion % 2 == 1) || (blank % 2 == 1 && inversion % 2 == 0)) board
            else scrambleBoard(board.shuffled())
        }

        return scrambleBoard(board.shuffled())
    }

    private fun countInversion(board: List<Int>): Int {
        var inversions = 0

        for (i in 0..<board.size - 1) {
            if (board[i] != 0) {
                for (j in i + 1..<board.size) {
                    if (board[j] != 0 && board[i] > board[j]) {
                        inversions++
                    }
                }
            }
        }

        return inversions
    }

    private fun findBlankPosition(index: Int): Int {
        val squareSize = size * size
        return (squareSize - index) / size + 1
    }
}