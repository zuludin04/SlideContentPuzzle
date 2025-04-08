package com.app.zuludin.slidecontentpuzzle.ui.puzzle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.zuludin.slidecontentpuzzle.R
import com.app.zuludin.slidecontentpuzzle.core.utils.Item
import com.app.zuludin.slidecontentpuzzle.core.view.AnimatedVerticalGrid
import com.app.zuludin.slidecontentpuzzle.core.view.PuzzleSolvedDialog

private const val PUZZLE_SIZE = 3

@Composable
fun PuzzleScreen(viewModel: PuzzleViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8de9d5),
                            Color(0xFF32c4c0)
                        )
                    )
                ),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.height(16.dp))
                Icon(
                    painterResource(R.drawable.ic_pin),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Text("Number Puzzle", fontSize = 18.sp, color = Color.White)
                Box(modifier = Modifier.height(16.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .border(5.dp, Color.White)
                    .padding(8.dp)
            ) {
                AnimatedVerticalGrid(
                    items = uiState.board,
                    itemKey = Item::id,
                    columns = PUZZLE_SIZE,
                    rows = PUZZLE_SIZE,
                ) { item ->
                    if (item.id != 0) {
                        Item(item) { id ->
                            val index = uiState.board.indexOfFirst { it.id == id }
                            viewModel.swipeTile(index, PUZZLE_SIZE, uiState.board)
                        }
                    } else {
                        Box(modifier = Modifier)
                    }
                }
            }

            Box(modifier = Modifier.height(64.dp))
            ElevatedButton(onClick = {}) { Text("Auto Solve") }
            Box(modifier = Modifier.height(64.dp))
        }
    }

    if (uiState.showSuccessDialog) {
        Dialog(onDismissRequest = { viewModel.dismissSuccessDialog() }) {
            PuzzleSolvedDialog()
        }
    }
}

@Composable
private fun Item(item: Item, onClick: (Int) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .background(item.color)
            .clickable {
                onClick(item.id)
            }
    ) {
        Text(item.id.toString())
    }
}