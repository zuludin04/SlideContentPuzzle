package com.app.zuludin.slidecontentpuzzle.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.zuludin.slidecontentpuzzle.R
import com.app.zuludin.slidecontentpuzzle.core.utils.BitmapSlicer
import com.app.zuludin.slidecontentpuzzle.core.utils.Item
import com.app.zuludin.slidecontentpuzzle.core.utils.createImageItems
import com.app.zuludin.slidecontentpuzzle.core.utils.initialImageItems
import com.app.zuludin.slidecontentpuzzle.core.view.AnimatedVerticalGrid

private const val PUZZLE_SIZE = 3

@Composable
fun PuzzlePhotoScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val context = LocalContext.current
        val bitmaps = spliceImage(context)

        var items by remember { mutableStateOf(createImageItems(PUZZLE_SIZE, bitmaps)) }
        var showDialog by remember { mutableStateOf(false) }

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
                    painterResource(R.drawable.ic_picture),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Text("Image Puzzle", fontSize = 18.sp, color = Color.White)
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

                            val initial =
                                initialImageItems(PUZZLE_SIZE * PUZZLE_SIZE, bitmaps).map { it.id }
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
        if (item.image != null)
            Image(item.image.asImageBitmap(), null, contentScale = ContentScale.Fit)
    }
}

fun spliceImage(context: Context): List<BitmapSlicer> {
    val imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.example)

    val splitWidth = imageBitmap.width / 3
    val splitHeight = imageBitmap.height / 3

    val result = mutableListOf<BitmapSlicer>()

    var x = 0
    var y = 0

    var id = 0

    for (i in 0..<3) {
        for (j in 0..<3) {
            val bitmap = Bitmap.createBitmap(imageBitmap, x, y, splitWidth, splitHeight)
            result.add(BitmapSlicer(id++, bitmap))
            x += splitWidth
        }

        x = 0
        y += splitHeight
    }

    return result
}