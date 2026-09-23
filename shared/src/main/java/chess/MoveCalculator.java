package chess;

import java.util.Collection;

public interface MoveCalculator {
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);
}
