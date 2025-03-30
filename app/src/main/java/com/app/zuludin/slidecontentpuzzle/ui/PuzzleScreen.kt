package com.app.zuludin.slidecontentpuzzle.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.zuludin.slidecontentpuzzle.R
import com.app.zuludin.slidecontentpuzzle.core.utils.Item
import com.app.zuludin.slidecontentpuzzle.core.utils.createItems
import com.app.zuludin.slidecontentpuzzle.core.utils.initialItems
import com.app.zuludin.slidecontentpuzzle.core.view.AnimatedVerticalGrid
import com.app.zuludin.slidecontentpuzzle.core.view.PuzzleSolvedDialog

private const val PUZZLE_SIZE = 4

@Composable
fun PuzzleScreen() {
    var items by remember { mutableStateOf(createItems(PUZZLE_SIZE)) }
    var showDialog by remember { mutableStateOf(true) }

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
                    items = items,
                    itemKey = Item::id,
                    columns = PUZZLE_SIZE,
                    rows = PUZZLE_SIZE,
                ) { item ->
                    if (item.id != 0) {
                        Item(item) { id ->
                            val index = items.indexOfFirst { it.id == id }
                            val swapped =
                                onSwipePuzzle(index, items)
                            items = swapped

                            val initial = initialItems(PUZZLE_SIZE * PUZZLE_SIZE).map { it.id }
                            showDialog = initial == items.map { it.id }
                        }
                    } else {
                        Box(modifier = Modifier)
                    }
                }
            }

            Box(modifier = Modifier.height(64.dp))
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            PuzzleSolvedDialog()
        }
    }
}

private fun onSwipePuzzle(index: Int, items: List<Item>): List<Item> {
    val data = items.toMutableList()

    val zeroItem = items.first { it.id == 0 }
    val emptyTilePosIndex = items.indexOf(zeroItem)
    val emptyTilePosRow = emptyTilePosIndex / PUZZLE_SIZE
    val emptyTilePosCol = emptyTilePosIndex % PUZZLE_SIZE

    val currentTileRow = index / PUZZLE_SIZE
    val currentTileCol = index % PUZZLE_SIZE

    if ((currentTileRow - 1 == emptyTilePosRow) &&
        (currentTileCol == emptyTilePosCol)
    ) {
        // up
        data[emptyTilePosIndex] = data[index]
        data[index] = zeroItem
    } else if ((currentTileRow + 1 == emptyTilePosRow) &&
        (currentTileCol == emptyTilePosCol)
    ) {
        // down
        data[emptyTilePosIndex] = data[index]
        data[index] = zeroItem
    } else if ((currentTileRow == emptyTilePosRow) &&
        (currentTileCol - 1 == emptyTilePosCol)
    ) {
        // left
        data[emptyTilePosIndex] = data[index]
        data[index] = zeroItem
    } else if ((currentTileRow == emptyTilePosRow) &&
        (currentTileCol + 1 == emptyTilePosCol)
    ) {
        // right
        data[emptyTilePosIndex] = data[index]
        data[index] = zeroItem
    }

    return data
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