package com.app.zuludin.slidecontentpuzzle.core.utils

import androidx.compose.ui.graphics.Color

class GameEngine(private var size: Int = 3) {
    fun buildSolvableBoard(board: List<Int>): List<Int> {
        val inversion = countInversion(board)

        if (size % 2 == 1) {
            if (inversion % 2 == 0) return board
            else buildSolvableBoard(board.shuffled())
        } else {
            val index = board.indexOf(0)
            val blank = findBlankPosition(index)
            return if ((blank % 2 == 0 && inversion % 2 == 1) || (blank % 2 == 1 && inversion % 2 == 0)) board
            else buildSolvableBoard(board.shuffled())
        }

        return buildSolvableBoard(board.shuffled())
    }

    fun generateGoalBoard(): List<Item> {
        val items = mutableListOf<Item>()
        for (i in 1..<size * size) {
            items.add(Item(i, Color.Cyan))
        }
        items.add(Item(0, Color.Cyan))
        return items.toList()
    }

    fun isValidDirection(
        currentTileRow: Int,
        currentTileCol: Int,
        emptyTilePosRow: Int,
        emptyTilePosCol: Int
    ): Boolean {
        return ((currentTileRow - 1 == emptyTilePosRow) && (currentTileCol == emptyTilePosCol)) ||
                ((currentTileRow + 1 == emptyTilePosRow) && (currentTileCol == emptyTilePosCol)) ||
                ((currentTileRow == emptyTilePosRow) && (currentTileCol - 1 == emptyTilePosCol)) ||
                ((currentTileRow == emptyTilePosRow) && (currentTileCol + 1 == emptyTilePosCol))
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