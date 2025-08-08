package com.google.codelab.gamingzone.domain.model

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.google.codelab.gamingzone.R
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GameItem(
    val id: String? =null,
    val name: String? ="PlayZone",
    val category: GameCategory? =GameCategory.SKILL,
    val description: String? ="Skill Game",
    val coverImageUrl: Int,
    val tutorialImageUrls: List<Int>? =listOf(
       R.drawable.freepik_chess,R.drawable.freepik_chess,R.drawable.freepik_chess,R.drawable.freepik_chess
    ),
    val gameImageUrls: List<String>,
    val difficultyLevels: List<String> = listOf("Easy", "Medium", "Hard"),
   // val buildScreen: (String) -> Routes
)

data class AllGames(
    val id: String?=null,
    val gameName: String?="PlayZone",
    val category: GameCategory? = GameCategory.SKILL,
    val gameImage: Drawable,
)

enum class GameCategory {
     SKILL, FUN, PUZZLE, REFLEX, LOGIC
}


