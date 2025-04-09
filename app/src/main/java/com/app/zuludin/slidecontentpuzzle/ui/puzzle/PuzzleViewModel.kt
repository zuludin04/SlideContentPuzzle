package com.app.zuludin.slidecontentpuzzle.ui.puzzle

import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.slidecontentpuzzle.core.utils.GameEngine
import com.app.zuludin.slidecontentpuzzle.core.utils.Item
import com.app.zuludin.slidecontentpuzzle.core.utils.Node
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class PuzzleUiState(
    val board: List<Item> = emptyList(),
    val showSuccessDialog: Boolean = false,
    val totalSteps: Int = 0,
    val timer: Long = 0L
)

class PuzzleViewModel : ViewModel() {
    private val gameEngine = GameEngine()
    private val goal = gameEngine.generateGoalBoard()

    private val _uiState = MutableStateFlow(PuzzleUiState())
    val uiState: StateFlow<PuzzleUiState> = _uiState.asStateFlow()

    private var _autoSolveNode: Node? = null
    private var _timerJob: Job? = null

    init {
        scrambleBoard()
    }

    fun scrambleBoard() {
        val shuffled = goal.map { it.id }.shuffled()
        val solvableBoard = gameEngine.buildSolvableBoard(shuffled)
        val initialBoard = gameEngine.convertTo2D(solvableBoard, 3)
        val goalBoard = gameEngine.convertTo2D(goal.map { it.id }, 3)

        val root = Node(
            g = 0,
            h = gameEngine.manhattanDistanceSum(initialBoard, goalBoard),
            f = gameEngine.manhattanDistanceSum(initialBoard, goalBoard) + 0,
            puzzle = initialBoard
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _autoSolveNode = null
            _autoSolveNode = gameEngine.solveAStarPuzzle(root, goalBoard)
        }

        _uiState.update {
            it.copy(
                timer = 0L,
                totalSteps = 0,
                board = solvableBoard.map { id -> Item(id, Color(0xFF00BCD4)) },
            )
        }
    }

    fun swipeTile(index: Int, puzzleSize: Int, items: List<Item>) {
        if (_timerJob == null) {
            startTimer()
        }

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
                showSuccessDialog = goal.map { g -> g.id } == data.map { d -> d.id },
                totalSteps = it.totalSteps + 1
            )
        }

        stopTimer()
    }

    fun dismissSuccessDialog() {
        _uiState.update { it.copy(showSuccessDialog = false) }
    }

    fun autoSolvePuzzle(board: List<Item>) {
        startTimer()

        val items = board.toMutableList()

        var currentNode = _autoSolveNode
        val directions = mutableStateListOf<Node>()

        while (currentNode != null) {
            directions.add(currentNode)
            currentNode = currentNode.parent
        }

        directions.reverse()

        viewModelScope.launch {
            for (step in 1..<directions.size) {
                items.clear()
                val newBoard = mutableListOf<Item>()

                directions[step].puzzle.forEach { d ->
                    d.forEach { newBoard.add(Item(it, Color.Cyan)) }
                }

                items.addAll(newBoard)

                delay(1000.milliseconds)
                _uiState.update {
                    it.copy(
                        totalSteps = it.totalSteps + 1,
                        board = items,
                        showSuccessDialog = goal.map { g -> g.id } == items.map { d -> d.id }
                    )
                }
            }

            stopTimer()
        }
    }

    private fun startTimer() {
        _uiState.update { it.copy(timer = 0L) }
        _timerJob?.cancel()
        _timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(timer = it.timer + 1) }
            }
        }
    }

    private fun stopTimer() {
        if (_uiState.value.showSuccessDialog) {
            _timerJob?.cancel()
            _timerJob = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        _timerJob?.cancel()
        _timerJob = null
    }
}