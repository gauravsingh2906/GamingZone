package com.google.codelab.gamingzone.presentation.games.chess_screen3

sealed class ChessAction {
    data class OnSquareSelected(val position: Position) : ChessAction()
    object OnGameDialogDismissed : ChessAction()
}
