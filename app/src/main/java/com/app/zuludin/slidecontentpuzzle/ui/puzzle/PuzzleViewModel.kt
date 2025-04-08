package com.app.zuludin.slidecontentpuzzle.ui.puzzle

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.app.zuludin.slidecontentpuzzle.core.utils.GameEngine
import com.app.zuludin.slidecontentpuzzle.core.utils.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PuzzleUiState(
    val board: List<Item> = emptyList(),
    val showSuccessDialog: Boolean = false
)

class PuzzleViewModel : ViewModel() {
    private val gameEngine = GameEngine()
    private val goal = gameEngine.generateGoalBoard()

    private val _uiState = MutableStateFlow(PuzzleUiState())
    val uiState: StateFlow<PuzzleUiState> = _uiState.asStateFlow()

    init {
        scrambleBoard()
    }

    fun scrambleBoard() {
        val shuffled = goal.map { it.id }.shuffled()
        val solvableBoard = gameEngine.buildSolvableBoard(shuffled)

        _uiState.update {
            it.copy(
                board = solvableBoard.map { id -> Item(id, Color(0xFF00BCD4)) }
            )
        }
    }

    fun swipeTile(index: Int, puzzleSize: Int, items: List<Item>) {
        val data = items.toMutableList()

        val zeroItem = items.first { it.id == 0 }
        val emptyTilePosIndex = items.indexOf(zeroItem)
        val emptyTilePosRow = emptyTilePosIndex / puzzleSize
        val emptyTilePosCol = emptyTilePosIndex % puzzleSize

        val currentTileRow = index / puzzleSize
        val currentTileCol = index % puzzleSize

        if (gameEngine.isValidDirection(
                currentTileRow,
                currentTileCol,
                emptyTilePosRow,
                emptyTilePosCol
            )
        ) {
            data[emptyTilePosIndex] = data[index]
            data[index] = zeroItem
        }

        _uiState.update {
            it.copy(
                board = data,
                showSuccessDialog = goal.map { g -> g.id } == data.map { d -> d.id }
            )
        }
    }

    fun dismissSuccessDialog() {
        _uiState.update { it.copy(showSuccessDialog = false) }
    }
}