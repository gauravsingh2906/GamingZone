package com.google.codelab.gamingzone.presentation.games.chess_screen3

enum class PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }
enum class PlayerColor {
    WHITE,
    BLACK;

    val opposite: PlayerColor
        get() = if (this == WHITE) BLACK else WHITE
}

enum class Player { HUMAN, AI }

data class Move(val from: Position, val to: Position)




data class ChessPiece(
    val type: PieceType,
    val color: PlayerColor,
    val position: Position,
    val hasMoved: Boolean = false
)


val ChessPiece.symbol: String
    get() = when (type) {
        PieceType.PAWN -> if (color == PlayerColor.WHITE) "♙" else "♟"
        PieceType.ROOK -> if (color == PlayerColor.WHITE) "♖" else "♜"
        PieceType.KNIGHT -> if (color == PlayerColor.WHITE) "♘" else "♞"
        PieceType.BISHOP -> if (color == PlayerColor.WHITE) "♗" else "♝"
        PieceType.QUEEN -> if (color == PlayerColor.WHITE) "♕" else "♛"
        PieceType.KING -> if (color == PlayerColor.WHITE) "♔" else "♚"
    }


