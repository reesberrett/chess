package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},     // up, down, right, left
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}    // Diagonal
        };

        for (int[] dir : directions) {
            int nextRow = r + dir[0];
            int nextCol = c + dir[1];

            if (nextRow >= 1 && nextRow <= 8 && nextCol >= 1 && nextCol <= 8) {
                ChessPosition nextPosition = new ChessPosition(nextRow, nextCol);
                ChessPiece targetPiece = board.getPiece(nextPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }

        // Castling
        if (c == 5) {
            boolean isWhite = (color == ChessGame.TeamColor.WHITE);
            boolean kingMoved = isWhite ? board.getWhiteKingMoved() : board.getBlackKingMoved();

            if (!kingMoved) {
                boolean rookRightMoved = isWhite ? board.getWhiteRookRightMoved() : board.getBlackRookRightMoved();
                boolean rookLeftMoved = isWhite ? board.getWhiteRookLeftMoved() : board.getBlackRookLeftMoved();

                // Kingside castle (columns 6 and 7 empty)
                ChessPosition gPosition = new ChessPosition(r, 7);
                if (!rookRightMoved && board.getPiece(new ChessPosition(r, 6)) == null && board.getPiece(gPosition) == null) {
                    moves.add(new ChessMove(myPosition, gPosition, null));
                }

                // Queenside castle (columns 2, 3, and 4 empty)
                ChessPosition cPosition = new ChessPosition(r, 3);
                if (!rookLeftMoved && board.getPiece(new ChessPosition(r, 4)) == null
                        && board.getPiece(cPosition) == null && board.getPiece(new ChessPosition(r, 2)) == null) {
                    moves.add(new ChessMove(myPosition, cPosition, null));
                }
            }
        }

        return moves;
    }

}
