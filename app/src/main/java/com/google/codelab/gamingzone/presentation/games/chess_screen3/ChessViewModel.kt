package com.google.codelab.gamingzone.presentation.games.chess_screen3

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.codelab.gamingzone.presentation.games.chess_screen3.ChessEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChessViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ChessState())
    val state: StateFlow<ChessState> = _state

    private var isAIMove = false

    private val engine = ChessEngine()

    fun onAction(action: ChessAction) {
        when (action) {
            is ChessAction.OnSquareSelected -> {
                onSquareClicked(action.position.row, action.position.col)
            }

            ChessAction.OnGameDialogDismissed -> {
                _state.value = ChessState() // reset game
            }
        }
    }



    fun onSquareClicked(row: Int, col: Int) {
        val selected = state.value.selectedPiece
        val clickedPiece = state.value.board[row][col]

        if (selected == null) {
            if (clickedPiece != null && clickedPiece.color == state.value.currentTurn) {
                val validMoves = engine.getValidMoves(clickedPiece, state.value.board)
                _state.update {
                    it.copy(selectedPiece = clickedPiece, validMoves = validMoves)
                }
            }
        } else {
            val destination = Position(row, col)
            if (state.value.validMoves.contains(destination)) {
                makeMove(selected, destination)
            } else {
                _state.update {
                    it.copy(selectedPiece = null, validMoves = emptyList())
                }
            }
        }
    }

    private fun makeMove(piece: ChessPiece, to: Position) {
        val board = state.value.board.map { it.toMutableList() }.toMutableList()
        board[piece.position.row][piece.position.col] = null
        val newPiece = piece.copy(position = to)
        board[to.row][to.col] = newPiece

        val current = piece.color
        val opponent = current.opposite

        val isCheck = engine.isKingInCheck(opponent, board)
        val hasMoves = engine.hasLegalMoves(opponent,board)

        val isCheckmate = isCheck && !hasMoves
        val isStalemate = !isCheck && !hasMoves

        val gameOver = isCheckmate || isStalemate

        _state.update {
            it.copy(
                board = board,
                selectedPiece = null,
                validMoves = emptyList(),
                currentTurn = opponent,
                isInCheck = isCheck,
                gameOver = gameOver,
                winner = when {
                    isCheckmate -> current
                    isStalemate -> null
                    else -> null
                }
            )
        }

        // Simple AI move after human plays
        if (!isCheckmate && opponent == PlayerColor.BLACK) {
            isAIMove = true
            makeAIMove()
        }
    }

    fun makeAIMove() {
        if (!isAIMove) return

        // Simulate AI thinking
        viewModelScope.launch {
            delay(1000)  // Simulate AI thinking time (1 second)

            // Now perform the move after thinking
            val aiMove = engine.generateAIMove(state.value.board, PlayerColor.BLACK)
            if (aiMove != null) {
                val piece = state.value.board[aiMove.from.row][aiMove.from.col]
                if (piece != null) {
                    makeMove(piece, aiMove.to)
                }
            }

            // Switch turn back to the player
            isAIMove = false
        }
    }

}



