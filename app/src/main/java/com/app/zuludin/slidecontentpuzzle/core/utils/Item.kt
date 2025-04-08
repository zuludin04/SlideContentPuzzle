package com.app.zuludin.slidecontentpuzzle.core.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class Item(
    val id: Int,
    val color: Color,
    val image: Bitmap? = null
)

fun initialImageItems(size: Int, bitmaps: List<BitmapSlicer>): List<Item> {
    val items = mutableListOf<Item>()
    for (i in 1..<size) {
        items.add(Item(i, Color.Cyan, bitmaps[i - 1].image))
    }
    items.add(Item(0, Color.Cyan))
    return items
}

fun createImageItems(count: Int, bitmaps: List<BitmapSlicer>): List<Item> {
    val puzzle = GameEngine(count)
    val shuffled = initialImageItems(count * count, bitmaps).map { it.id }.shuffled()
    val scramble = puzzle.buildSolvableBoard(shuffled)
    return scramble.map {
        if (it != 0) Item(it, Color(0xFF00BCD4), bitmaps[it - 1].image)
        else Item(it, Color(0xFF00BCD4))
    }
}