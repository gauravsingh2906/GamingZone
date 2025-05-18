package com.google.codelab.gamingzone.presentation.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.domain.model.GameItem
import com.google.codelab.gamingzone.domain.model.GameCategory
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.sampleGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

//    private val _selectedCategory = MutableStateFlow<GameCategory?>(null)
//    val selectedCategory: StateFlow<GameCategory?> = _selectedCategory



  //  val games = mutableStateOf<List<GameItem>>(emptyList())

   private val _selectedCategory = mutableStateOf<GameCategory?>(null)
    val selectedCategory: State<GameCategory?> = _selectedCategory

    private val allGames = sampleGames



    val games: State<List<GameItem>> = derivedStateOf {
        _selectedCategory.value?.let { category ->
            allGames.filter { it.category == category }
        } ?: allGames
    }

    fun updateCategory(category: GameCategory?) {
        _selectedCategory.value = category
    }






//    private val _selectedCategory = MutableStateFlow<GameCategory?>(null)
//    val selectedCategory = _selectedCategory.asStateFlow()

    fun onCategorySelected(category: GameCategory?) {
        _selectedCategory.value = category
    }


}