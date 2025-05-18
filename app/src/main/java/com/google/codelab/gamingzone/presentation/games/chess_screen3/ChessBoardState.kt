package com.google.codelab.gamingzone.presentation.games.chess_screen3

import kotlin.random.Random

typealias ChessBoard = Array<Array<ChessPiece?>>

//fun getInitialBoard(): Array<Array<ChessPiece?>> {
//    val board = Array(8) { arrayOfNulls<ChessPiece>(8) }
//
//    for (col in 0..7) {
//        board[1][col] = ChessPiece(PieceType.PAWN, PlayerColor.BLACK)
//        board[6][col] = ChessPiece(PieceType.PAWN, PlayerColor.WHITE)
//    }
//
//    return board
//}

//fun getPossibleMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Pair<Int, Int>> {
//    return when (piece.type) {
//        PieceType.PAWN -> getPawnMoves(piece, board)
//        PieceType.ROOK -> getRookMoves(piece, board)
//        PieceType.BISHOP -> getBishopMoves(piece, board)
//        PieceType.KNIGHT -> getKnightMoves(piece, board)
//        PieceType.QUEEN -> getQueenMoves(piece, board)
//        PieceType.KING -> getKingMoves(piece, board)
//    }
//}





fun makeRandomAIMove(board: ChessBoard): ChessBoard {
    val blackPieces = mutableListOf<Pair<Int, Int>>()

    for (row in 0..7) {
        for (col in 0..7) {
            val piece = board[row][col]
            if (piece != null && piece.color == PlayerColor.BLACK) {
                blackPieces.add(row to col)
            }
        }
    }

    val newBoard = board.map { it.clone() }.toTypedArray()

    for (attempt in 1..100) {
        val (fromRow, fromCol) = blackPieces.random()
        val toRow = Random.nextInt(0, 8)
        val toCol = Random.nextInt(0, 8)

        // AI just makes a move if target cell is empty (basic logic)
        if (newBoard[toRow][toCol] == null) {
            newBoard[toRow][toCol] = newBoard[fromRow][fromCol]
            newBoard[fromRow][fromCol] = null
            return newBoard
        }
    }

    return newBoard // return unchanged if no move made
}

