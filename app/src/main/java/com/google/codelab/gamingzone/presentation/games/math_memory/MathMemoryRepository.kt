package com.google.codelab.gamingzone.presentation.games.math_memory

interface MathMemoryRepository {
    suspend fun getUnlockedThemes(): Set<String> // theme names or ids
    suspend fun addUnlockedTheme(themeName: String)
    suspend fun getSelectedTheme(): String
    suspend fun setSelectedTheme(themeName: String)
    // ...level progress methods...
}
