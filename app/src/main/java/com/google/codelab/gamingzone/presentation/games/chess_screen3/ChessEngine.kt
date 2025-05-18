package com.google.codelab.gamingzone.presentation.games.chess_screen3

class ChessEngine {

    fun generateAIMove(board: List<List<ChessPiece?>>, color: PlayerColor): Move? {
        val pieces = board.flatten().filter { it?.color == color }.filterNotNull()
        val allMoves = pieces.flatMap { piece ->
            getValidMoves(piece, board).map { dest -> Move(piece.position, dest) }
        }
        return allMoves.randomOrNull()
    }

    fun getValidMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        return when (piece.type) {
            PieceType.PAWN -> getPawnMoves(piece, board)
            PieceType.ROOK -> getRookMoves(piece, board)
            PieceType.BISHOP -> getBishopMoves(piece, board)
            PieceType.KNIGHT -> getKnightMoves(piece, board)
            PieceType.QUEEN -> getQueenMoves(piece, board)
            PieceType.KING -> getKingMoves(piece, board)
        }
    }

    fun isKingInCheck(color: PlayerColor, board: List<List<ChessPiece?>>): Boolean {
        val king = board.flatten().firstOrNull {
            it?.type == PieceType.KING && it.color == color
        } ?: return false

        val opponentPieces = board.flatten().filter { it?.color == color.opposite }
        return opponentPieces.any {
            it != null && getValidMoves(it, board).contains(king.position)
        }
    }

    fun hasLegalMoves(color: PlayerColor, board: List<List<ChessPiece?>>): Boolean {
        val pieces = board.flatten().filter { it?.color == color }
        return pieces.any {
            it != null && getValidMoves(it, board).any { move ->
                !isKingInCheck(color, simulateMove(board, it, move))
            }
        }
    }

    fun simulateMove(board: List<List<ChessPiece?>>, piece: ChessPiece, to: Position): List<List<ChessPiece?>> {
        val copy = board.map { it.toMutableList() }.toMutableList()
        copy[piece.position.row][piece.position.col] = null
        copy[to.row][to.col] = piece.copy(position = to)
        return copy
    }

    // Helper methods

    private fun getPawnMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        val direction = if (piece.color == PlayerColor.WHITE) -1 else 1
        val moves = mutableListOf<Position>()
        val row = piece.position.row
        val col = piece.position.col

        // One step forward
        if (isInBounds(row + direction, col) && board[row + direction][col] == null) {
            moves.add(Position(row + direction, col))
            // Two steps on first move
            if ((piece.color == PlayerColor.WHITE && row == 6) || (piece.color == PlayerColor.BLACK && row == 1)) {
                if (board[row + 2 * direction][col] == null) {
                    moves.add(Position(row + 2 * direction, col))
                }
            }
        }

        // Capture diagonals
        for (dCol in listOf(-1, 1)) {
            val newCol = col + dCol
            if (isInBounds(row + direction, newCol)) {
                val target = board[row + direction][newCol]
                if (target != null && target.color != piece.color) {
                    moves.add(Position(row + direction, newCol))
                }
            }
        }

        return moves
    }

    private fun getRookMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        return generateMoves(piece, board, listOf(
            1 to 0, -1 to 0, 0 to 1, 0 to -1
        ))
    }

    private fun getBishopMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        return generateMoves(piece, board, listOf(
            1 to 1, 1 to -1, -1 to 1, -1 to -1
        ))
    }

    private fun getQueenMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        return getRookMoves(piece, board) + getBishopMoves(piece, board)
    }

    private fun getKnightMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        val directions = listOf(
            -2 to -1, -2 to 1, -1 to -2, -1 to 2,
            1 to -2, 1 to 2, 2 to -1, 2 to 1
        )
        return directions.mapNotNull { (dr, dc) ->
            val r = piece.position.row + dr
            val c = piece.position.col + dc
            if (isInBounds(r, c) && board[r][c]?.color != piece.color) Position(r, c) else null
        }
    }

    private fun getKingMoves(piece: ChessPiece, board: List<List<ChessPiece?>>): List<Position> {
        val directions = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )
        return directions.mapNotNull { (dr, dc) ->
            val r = piece.position.row + dr
            val c = piece.position.col + dc
            if (isInBounds(r, c) && board[r][c]?.color != piece.color) Position(r, c) else null
        }
    }

    private fun generateMoves(
        piece: ChessPiece,
        board: List<List<ChessPiece?>>,
        directions: List<Pair<Int, Int>>
    ): List<Position> {
        val moves = mutableListOf<Position>()
        for ((dr, dc) in directions) {
            var r = piece.position.row + dr
            var c = piece.position.col + dc
            while (isInBounds(r, c)) {
                val target = board[r][c]
                if (target == null) {
                    moves.add(Position(r, c))
                } else {
                    if (target.color != piece.color) moves.add(Position(r, c))
                    break
                }
                r += dr
                c += dc
            }
        }
        return moves
    }

    private fun isInBounds(row: Int, col: Int): Boolean {
        return row in 0..7 && col in 0..7
    }
}


