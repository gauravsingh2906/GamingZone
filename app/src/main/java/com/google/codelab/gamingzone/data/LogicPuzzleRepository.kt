package com.google.codelab.gamingzone.data

import android.content.Context
import android.util.Log
import com.google.codelab.gamingzone.domain.logic_story.LogicPuzzle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LogicPuzzleRepository @Inject constructor( @ApplicationContext private val context: Context) {



    fun loadAllPuzzles(): List<LogicPuzzle> {
        try {
            val files = context.assets.list("") ?: emptyArray()
            Log.d("AssetsDebug", "Assets found: ${files.joinToString()}")


            val json = context.assets.open("loggic_puzzles.json")
                .bufferedReader().use { it.readText() }

            val type = object : TypeToken<List<LogicPuzzle>>() {}.type
            return Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Could not read logic_puzzles.json from assets!", e)
        }
    }

}
