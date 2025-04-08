package com.app.zuludin.slidecontentpuzzle.core.utils

data class Node(
    val g: Int,
    val h: Int,
    val f: Int,
    val parent: Node? = null,
    val puzzle: Array<IntArray>,
    val direction: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        return puzzle.contentDeepEquals(other.puzzle)
    }

    override fun hashCode(): Int {
        return puzzle.contentDeepHashCode()
    }
}
