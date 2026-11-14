package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int[][] directions = {
                {1, 1},   // Up-right
                {1, -1},  // Up-left
                {-1, 1},  // Down-right
                {-1, -1}  // Down-left
        };

        for (int[] dir : directions) {
            int r = myPosition.getRow() + dir[0];
            int c = myPosition.getColumn() + dir[1];

            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                ChessPosition nextPosition = new ChessPosition(r, c);
                ChessPiece nextPiece = board.getPiece(nextPosition);

                if (nextPiece == null) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                } else {
                    if (nextPiece.getTeamColor() != color) {
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
