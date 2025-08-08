package com.google.codelab.gamingzone.domain.math_dungeon

interface MathDungeonRepository {
    fun generateDungeon(rows: Int, cols: Int): List<List<Tile>>
    fun generateQuestion(): MathQuestion
}
