package com.google.codelab.gamingzone.data.mapper

import androidx.room.TypeConverter
import com.google.codelab.gamingzone.data.model.GameStats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun toList(data: String): List<Int> = if (data.isEmpty()) listOf() else data.split(",").map { it.toInt() }


    private val gson = Gson()
    private val type = object : TypeToken<Map<String, GameStats>>(){}.type




}
