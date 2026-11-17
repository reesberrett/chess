package chess.moves;

import java.util.ArrayList;
import java.util.Collection;

import chess.*;

public class LateralMovesCalculator extends MoveCalculator {
    private final int[][] directions;

    //Takes in directional map for sliding pieces (rook, bishop, queen)
    public LateralMovesCalculator(int[][] directions) {
        this.directions = directions;
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //Iterating through all possible direction paths
        for (int[] dir : directions) {
            int r = myPosition.getRow() + dir[0];
            int c = myPosition.getColumn() + dir[1];

            //Iterate through all possible positions in each path
            while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                ChessPosition nextPosition = new ChessPosition(r, c);
                ChessPiece targetPiece = board.getPiece(nextPosition);

                //If no piece in position, add move to collection
                if (targetPiece == null) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                } else {
                    //If piece in position, add move capture, then stop
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