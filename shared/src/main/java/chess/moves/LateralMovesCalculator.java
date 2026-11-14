package chess.moves;

import java.util.ArrayList;
import java.util.Collection;

import chess.*;

public class LateralMovesCalculator extends MoveCalculator {
    private final int[][] directions;

    public LateralMovesCalculator(int[][] directions) {
        this.directions = directions;
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

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