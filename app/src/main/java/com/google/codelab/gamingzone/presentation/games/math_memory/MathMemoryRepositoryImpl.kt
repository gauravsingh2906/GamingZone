package com.google.codelab.gamingzone.presentation.games.math_memory

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("math_memory_prefs")

class MathMemoryRepositoryImpl(
    private val context: Context,
    private val themeList: List<GameTheme>
): MathMemoryRepository {

    companion object {
        private val UNLOCKED_THEMES = stringSetPreferencesKey("unlocked_themes")
        private val SELECTED_THEME = stringPreferencesKey("selected_theme")
        // Add more keys as needed (e.g., highestLevel, score)
    }

    override suspend fun getUnlockedThemes(): Set<String> {
        val prefs = context.dataStore.data.first()
        return prefs[UNLOCKED_THEMES] ?: setOf(themeList.first().name)
    }

    override suspend fun addUnlockedTheme(themeName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[UNLOCKED_THEMES] ?: setOf(themeList.first().name)
            prefs[UNLOCKED_THEMES] = current + themeName
        }
    }

    override suspend fun getSelectedTheme(): String {
        val prefs = context.dataStore.data.first()
        return prefs[SELECTED_THEME] ?: themeList.first().name
    }

    override suspend fun setSelectedTheme(themeName: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_THEME] = themeName
        }
    }
}
