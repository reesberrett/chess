package chess;

import java.util.Collection;
import java.util.ArrayList;

public abstract class MoveCalculator {
    public abstract Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);

    protected Collection<ChessMove> calculateSingleStepMoves(ChessBoard board, ChessPosition myPosition, int[][] offsets) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        for (int[] offset : offsets) {
            int nextRow = r + offset[0];
            int nextCol = c + offset[1];

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
