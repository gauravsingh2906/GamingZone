package com.google.codelab.gamingzone.domain.masyu_puzzle

enum class DotType { WHITE, BLACK }

data class Dot(
    val row: Int,
    val col: Int,
    val dot: DotType? = null
)

data class MasyuPuzzle(val size: Int, val dots: List<Dot>)
