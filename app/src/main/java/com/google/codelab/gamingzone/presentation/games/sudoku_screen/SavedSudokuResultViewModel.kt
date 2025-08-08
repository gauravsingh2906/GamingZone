package com.google.codelab.gamingzone.presentation.games.sudoku_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSudokuResultsViewModel @Inject constructor(
    private val dao: SudokuResultDao
) : ViewModel() {

    private val _results = mutableStateOf<List<SudokuResultEntity>>(emptyList())
    val results: State<List<SudokuResultEntity>> = _results

    init {
        viewModelScope.launch {
            _results.value = dao.getAllResults()
        }
    }
}
