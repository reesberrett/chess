package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements MoveCalculator{

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;

        int nextRow = r + direction;

        // 1. Moving forward (1 or 2 steps)
        if (nextRow >= 1 && nextRow <= 8) {
            ChessPosition oneStepPos = new ChessPosition(nextRow, c);
            if (board.getPiece(oneStepPos) == null) {
                addMoveWithPromotion(moves, myPosition, oneStepPos, nextRow == promotionRow);

                // Moving 2 steps forward from start
                if (r == startRow) {
                    ChessPosition twoStepPos = new ChessPosition(r + (2 * direction), c);
                    if (board.getPiece(twoStepPos) == null) {
                        moves.add(new ChessMove(myPosition, twoStepPos, null));
                    }
                }
            }
        }

        // 2. Diagonal captures & En Passant (left = -1, right = +1)
        ChessPosition epPosition = board.getEnPassantPosition();
        int[] captureOffsets = {-1, 1};

        for (int offset : captureOffsets) {
            int nextCol = c + offset;
            if (nextRow >= 1 && nextRow <= 8 && nextCol >= 1 && nextCol <= 8) {
                ChessPosition capturePos = new ChessPosition(nextRow, nextCol);
                ChessPiece targetPiece = board.getPiece(capturePos);

                // Standard diagonal capture
                if (targetPiece != null && targetPiece.getTeamColor() != color) {
                    addMoveWithPromotion(moves, myPosition, capturePos, nextRow == promotionRow);
                }
                // En Passant capture
                else if (capturePos.equals(epPosition)) {
                    moves.add(new ChessMove(myPosition, epPosition, null));
                }
            }
        }

        return moves;
    }

    private void addMoveWithPromotion(Collection<ChessMove> moves, ChessPosition start, ChessPosition end, boolean isPromotion) {
        if (isPromotion) {
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

}
