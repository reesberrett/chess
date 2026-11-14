package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},     // Orthogonal (up, down, right, left)
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}    // Diagonal
        };

        for (int[] dir : directions) {
            int r = myPosition.getRow() + dir[0];
            int c = myPosition.getColumn() + dir[1];

            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                ChessPosition nextPosition = new ChessPosition(r, c);
                ChessPiece targetPiece = board.getPiece(nextPosition);

                if (targetPiece == null) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                } else {
                    if (targetPiece.getTeamColor() != color) {
                        moves.add(new ChessMove(myPosition, nextPosition, null));
                    }
                    break;
                }

                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

}
