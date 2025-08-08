package com.google.codelab.gamingzone.domain.math_dungeon

import javax.inject.Inject
import kotlin.random.Random

class MathDungeonRepositoryImpl @Inject constructor() : MathDungeonRepository {
    override fun generateDungeon(rows: Int, cols: Int): List<List<Tile>> {
        return List(rows) { row ->
            List(cols) { col ->
                val isExit = row == rows - 1 && col == cols - 1
                val type = when {
                    isExit -> TileType.Exit
                    Random.nextFloat() < 0.2 -> TileType.Potion
                    Random.nextFloat() < 0.8 -> TileType.Trap
                    else -> TileType.Empty
                }
                Tile(
                    row = row,
                    col = col,
                    type = type,
                    question = if (type == TileType.Trap) generateQuestion() else null
                )
            }
        }
    }

    override fun generateQuestion(): MathQuestion {
        val a = (2..10).random()
        val b = (2..10).random()
        return MathQuestion("$a × $b = ?", a * b)
    }
}

