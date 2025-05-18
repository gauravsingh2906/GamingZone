package com.google.codelab.gamingzone.domain.model

import android.annotation.SuppressLint
import com.google.codelab.gamingzone.presentation.navigation.Routes
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GameItem(
    val id: String? =null,
    val name: String? ="PlayZone",
    val category: GameCategory? =GameCategory.SKILL,
    val description: String? ="Skill Game",
    val coverImageUrl: String,
    val tutorialImageUrls: List<String>? =listOf(
        "https://cdn.pixabay.com/photo/2015/08/02/17/20/sudoku-872218_960_720.jpg",
        "https://cdn.pixabay.com/photo/2017/01/10/15/36/sudoku-1963928_960_720.jpg",
        "https://cdn.pixabay.com/photo/2017/01/10/15/39/sudoku-1963931_960_720.jpg"
    ),
    val gameImageUrls: List<String>,
    val difficultyLevels: List<String> = listOf("Easy", "Medium", "Hard"),
    val buildScreen: (String) -> Routes
)

enum class GameCategory {
    SKILL, FUN, PUZZLE, REFLEX, LOGIC
}
