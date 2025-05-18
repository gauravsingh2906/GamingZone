package com.google.codelab.gamingzone.presentation.games.chess_screen3



data class ChessState(
    val board: List<List<ChessPiece?>> = createInitialBoard(),
    val selectedPiece: ChessPiece? = null,
    val validMoves: List<Position> = emptyList(),
    val currentTurn: PlayerColor = PlayerColor.WHITE,
    val isInCheck: Boolean = false,
    val gameOver: Boolean = false,
    val winner: PlayerColor? = null
)

data class Position(
    val row: Int,
    val col: Int
)



fun createInitialBoard(): List<List<ChessPiece?>> {
    val board = MutableList(8) { MutableList<ChessPiece?>(8) { null } }

    fun placeRow(row: Int, color: PlayerColor) {
        board[row][0] = ChessPiece(PieceType.ROOK, color, Position(row, 0))
        board[row][1] = ChessPiece(PieceType.KNIGHT, color, Position(row, 1))
        board[row][2] = ChessPiece(PieceType.BISHOP, color, Position(row, 2))
        board[row][3] = ChessPiece(PieceType.QUEEN, color, Position(row, 3))
        board[row][4] = ChessPiece(PieceType.KING, color, Position(row, 4))
        board[row][5] = ChessPiece(PieceType.BISHOP, color, Position(row, 5))
        board[row][6] = ChessPiece(PieceType.KNIGHT, color, Position(row, 6))
        board[row][7] = ChessPiece(PieceType.ROOK, color, Position(row, 7))
    }

    placeRow(0, PlayerColor.BLACK)
    placeRow(7, PlayerColor.WHITE)

    for (col in 0..7) {
        board[1][col] = ChessPiece(PieceType.PAWN, PlayerColor.BLACK, Position(1, col))
        board[6][col] = ChessPiece(PieceType.PAWN, PlayerColor.WHITE, Position(6, col))
    }

    return board
}



