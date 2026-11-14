package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        int[][] directions = {
                {1, -2},  // Up 1, left 2
                {-1, -2}, // Down 1, left 2
                {2, -1},  // Up 2, left 1
                {2, 1},   // Up 2, right 1
                {1, 2},   // Up 1, right 2
                {-1, 2},  // Down 1, right 2
                {-2, 1},  // Down 2, right 1
                {-2, -1}  // Down 2, left 1
        };

        for (int[] jump : directions) {
            int nextRow = r + jump[0];
            int nextCol = c + jump[1];

            if (nextRow >= 1 && nextRow <= 8 && nextCol >= 1 && nextCol <= 8) {
                ChessPosition nextPosition = new ChessPosition(nextRow, nextCol);
                ChessPiece targetPiece = board.getPiece(nextPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }

        return moves;
    }

}
