package com.app.zuludin.slidecontentpuzzle.core.utils

fun Long.formatTimer(): String {
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return "$minutes:$seconds"
}