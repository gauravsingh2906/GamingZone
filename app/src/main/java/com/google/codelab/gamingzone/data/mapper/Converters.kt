package com.google.codelab.gamingzone.data.mapper

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun toList(data: String): List<Int> = if (data.isEmpty()) listOf() else data.split(",").map { it.toInt() }
}
