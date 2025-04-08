package com.app.zuludin.slidecontentpuzzle.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.util.PriorityQueue
import kotlin.math.abs

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

    @RequiresApi(Build.VERSION_CODES.N)
    fun solveAStarPuzzle(node: Node, goalState: Array<IntArray>): Node {
        val openList = PriorityQueue<Node>(compareBy { it.f })
        val openMoves = mutableListOf(node)
        val closeMoves = mutableSetOf<Array<IntArray>>()

        openList.add(node)
        var currentNode = node

        while (!checkPuzzleEqual(currentNode.puzzle, goalState)) {
            val neighbors = createPossibleNeighbor(currentNode)
            val distanceToNeighbors = currentNode.g + 1

            closeMoves.add(currentNode.puzzle)
            currentNode = openList.poll()!!

            for (n in neighbors) {
                if (closeMoves.contains(n.puzzle)) continue

                val index = openMoves.indexOfFirst { checkPuzzleEqual(n.puzzle, it.puzzle) }

                if (index != -1) {
                    val nn = openMoves[index]
                    if (nn.g > distanceToNeighbors) {
                        openMoves.removeAt(index)

                        openList.add(n)
                        openMoves.add(n)
                    }
                } else {
                    openList.add(n)
                    openMoves.add(n)
                }
            }
        }

        return currentNode
    }

    private fun checkPuzzleEqual(current: Array<IntArray>, visited: Array<IntArray>): Boolean {
        for (row in current.indices) {
            for (column in current.indices) {
                if (current[row][column] != visited[row][column]) {
                    return false
                }
            }
        }

        return true
    }

    private fun blankPosition(board: Array<IntArray>): Pair<Int, Int> {
        for (row in board.indices) {
            for (column in board[row].indices) {
                if (board[row][column] == 0) {
                    return Pair(column, row)
                }
            }
        }
        return Pair(-1, -1)
    }

    private fun puzzleMap(puzzle: Array<IntArray>): Map<Int, Pair<Int, Int>> {
        val map = mutableMapOf<Int, Pair<Int, Int>>()
        for (row in puzzle.indices) {
            for (column in puzzle[row].indices) {
                if (puzzle[row][column] != 0) {
                    val key = puzzle[row][column]
                    map[key] = Pair(row, column)
                }
            }
        }

        return map
    }

    fun manhattanDistanceSum(current: Array<IntArray>, goal: Array<IntArray>): Int {
        val scramblePuzzleMap = puzzleMap(current)
        val goalPuzzleMap = puzzleMap(goal)

        var manhattan = 0

        scramblePuzzleMap.forEach { (t, u) ->
            val goalTile = goalPuzzleMap[t]
            val first = abs(u.first - goalTile!!.first)
            val second = abs(u.second - goalTile.second)
            val manhattanDistance = first + second
            manhattan += manhattanDistance
        }

        return manhattan
    }

    private fun createPossibleNeighbor(node: Node): List<Node> {
        val neighbors = mutableListOf<Node>()
        val blank = blankPosition(node.puzzle)
        val goalBoard = arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(4, 5, 6),
            intArrayOf(7, 8, 0)
        )

        if (blank.second - 1 >= 0) {
            // swipe up
            val newPuzzle = swipeUp(node.puzzle, blank)
            val newNode = Node(
                g = node.g + 1,
                h = manhattanDistanceSum(newPuzzle, goalBoard),
                f = (node.g + 1) + manhattanDistanceSum(newPuzzle, goalBoard),
                parent = node,
                puzzle = newPuzzle,
                direction = "UP"
            )

            neighbors.add(newNode)
        }

        if (blank.second + 1 < 3) {
            // swipe down
            val newPuzzle = swipeBottom(node.puzzle, blank)
            val newNode = Node(
                g = node.g + 1,
                h = manhattanDistanceSum(newPuzzle, goalBoard),
                f = (node.g + 1) + manhattanDistanceSum(newPuzzle, goalBoard),
                parent = node,
                puzzle = newPuzzle,
                direction = "DOWN"
            )

            neighbors.add(newNode)
        }

        if (blank.first - 1 >= 0) {
            // swipe left
            val newPuzzle = swipeLeft(node.puzzle, blank)
            val newNode = Node(
                g = node.g + 1,
                h = manhattanDistanceSum(newPuzzle, goalBoard),
                f = (node.g + 1) + manhattanDistanceSum(newPuzzle, goalBoard),
                parent = node,
                puzzle = newPuzzle,
                direction = "LEFT"
            )

            neighbors.add(newNode)
        }

        if (blank.first + 1 < 3) {
            // swipe right
            val newPuzzle = swipeRight(node.puzzle, blank)
            val newNode = Node(
                g = node.g + 1,
                h = manhattanDistanceSum(newPuzzle, goalBoard),
                f = (node.g + 1) + manhattanDistanceSum(newPuzzle, goalBoard),
                parent = node,
                puzzle = newPuzzle,
                direction = "RIGHT"
            )

            neighbors.add(newNode)
        }

        return neighbors
    }

    private fun swipeBottom(board: Array<IntArray>, position: Pair<Int, Int>): Array<IntArray> {
        val target = board[position.second + 1][position.first]
        val result = mutableListOf<IntArray>()
        for (row in board.indices) {
            val tiles = mutableListOf<Int>()
            for (column in board[row].indices) {
                if (board[row][column] == 0) {
                    tiles.add(target)
                } else if (board[row][column] == target) {
                    tiles.add(0)
                } else {
                    tiles.add(board[row][column])
                }
            }
            result.add(tiles.toIntArray())
        }

        return result.toTypedArray()
    }

    private fun swipeLeft(board: Array<IntArray>, position: Pair<Int, Int>): Array<IntArray> {
        val target = board[position.second][position.first - 1]
        val result = mutableListOf<IntArray>()
        for (row in board.indices) {
            val tiles = mutableListOf<Int>()
            for (column in board[row].indices) {
                if (board[row][column] == 0) {
                    tiles.add(target)
                } else if (board[row][column] == target) {
                    tiles.add(0)
                } else {
                    tiles.add(board[row][column])
                }
            }
            result.add(tiles.toIntArray())
        }

        return result.toTypedArray()
    }

    private fun swipeRight(board: Array<IntArray>, position: Pair<Int, Int>): Array<IntArray> {
        val target = board[position.second][position.first + 1]
        val result = mutableListOf<IntArray>()
        for (row in board.indices) {
            val tiles = mutableListOf<Int>()
            for (column in board[row].indices) {
                if (board[row][column] == 0) {
                    tiles.add(target)
                } else if (board[row][column] == target) {
                    tiles.add(0)
                } else {
                    tiles.add(board[row][column])
                }
            }
            result.add(tiles.toIntArray())
        }

        return result.toTypedArray()
    }

    private fun swipeUp(board: Array<IntArray>, position: Pair<Int, Int>): Array<IntArray> {
        val target = board[position.second - 1][position.first]
        val result = mutableListOf<IntArray>()
        for (row in board.indices) {
            val tiles = mutableListOf<Int>()
            for (column in board[row].indices) {
                if (board[row][column] == 0) {
                    tiles.add(target)
                } else if (board[row][column] == target) {
                    tiles.add(0)
                } else {
                    tiles.add(board[row][column])
                }
            }
            result.add(tiles.toIntArray())
        }

        return result.toTypedArray()
    }

    fun convertTo2D(board1D: List<Int>, size: Int): Array<IntArray> {
        val numRows = (board1D.size + size - 1) / size  // Calculate the number of rows (chunks)

        // Create a 2D array with the calculated number of rows
        val chunks = Array(numRows) { IntArray(size) }

        // Fill the 2D array with values from the 1D array
        for (i in board1D.indices) {
            val row = i / size  // Determine which row the current element belongs to
            val col = i % size  // Determine the column index within the row
            chunks[row][col] = board1D[i]
        }

        return chunks
    }
}