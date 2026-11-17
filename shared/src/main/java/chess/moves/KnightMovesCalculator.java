package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends MoveCalculator {

    //Knight moves 2 spaces in any direction, then 1 space perpendicularly
    private static final int[][] DIRECTIONS = {
            {1, -2}, {-1, -2}, {2, -1}, {2, 1},
            {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}
    };

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return calculateSingleStepMoves(board, myPosition, DIRECTIONS);
    }

}
