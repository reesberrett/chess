package chess.moves;

import chess.*;

import java.util.Collection;

public class KingMovesCalculator extends MoveCalculator {

    //Moves one step in any direction (not counting checks)
    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = calculateSingleStepMoves(board, myPosition, DIRECTIONS);
        //Special moves: castling
        addCastlingMoves(board, myPosition, moves);

        return moves;
    }

    private void addCastlingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int r = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //King must not have previously moved
        boolean isWhite = (color == ChessGame.TeamColor.WHITE);
        if ((isWhite && board.getWhiteKingMoved()) || (!isWhite && board.getBlackKingMoved())) {
            return;
        }

        boolean rookRightMoved = isWhite ? board.getWhiteRookRightMoved() : board.getBlackRookRightMoved();
        boolean rookLeftMoved = isWhite ? board.getWhiteRookLeftMoved() : board.getBlackRookLeftMoved();

        //Kingside castle: Rook on col 8 hasn't moved, and cols 6 & 7 are empty
        if (!rookRightMoved && isRookAt(board, r, 8, color)
                && isEmpty(board, r, 6) && isEmpty(board, r, 7)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(r, 7), null));
        }

        //Queenside castle: Rook on col 1 hasn't moved, and cols 2, 3, & 4 are empty
        if (!rookLeftMoved && isRookAt(board, r, 1, color)
                && isEmpty(board, r, 4) && isEmpty(board, r, 3) && isEmpty(board, r, 2)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(r, 3), null));
        }
    }

    private boolean isEmpty(ChessBoard board, int row, int col) {
        return board.getPiece(new ChessPosition(row, col)) == null;
    }

    private boolean isRookAt(ChessBoard board, int row, int col, ChessGame.TeamColor color) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        return piece != null && piece.getTeamColor() == color && piece.getPieceType() == ChessPiece.PieceType.ROOK;
    }
}
