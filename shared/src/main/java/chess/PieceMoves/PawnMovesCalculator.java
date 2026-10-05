package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

/*public class PawnMovesCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        int direction = 1;
        int startRow = 2;
        int promotionRow = 8;

        //Swap sides if black
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = -1;
            startRow = 7;
            promotionRow = 1;
        }

        int nextRow = r + direction;
        ChessPosition nextPosition = new ChessPosition(nextRow, c);
        ChessPiece nextPiece = board.getPiece(nextPosition);

        /*
            Moving forward
        *//*

        if (nextRow >= 1 && nextRow <= 8 && nextPiece == null) {
            //promotion
            if (nextRow == promotionRow) {
                moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.QUEEN));
            }

            //normal step
            else
                moves.add(new ChessMove(myPosition, nextPosition, null));

            //moving 2 steps forward from start
            if (r == startRow) {
                ChessPosition nextTwoPosition = new ChessPosition(r + (2 * direction), c);
                ChessPiece nextTwoPiece = board.getPiece(nextTwoPosition);

                if (nextTwoPiece == null)
                    moves.add(new ChessMove(myPosition, nextTwoPosition, null));
            }
        }


        /*
            Capturing diagonally
        *//*

        //capture left
        if (nextRow >= 1 && nextRow <= 8 && c - 1 >= 1) {
            ChessPosition leftCapturePos = new ChessPosition(r + direction, c - 1);
            ChessPiece leftCapturePiece = board.getPiece(leftCapturePos);

            if (leftCapturePiece != null && leftCapturePiece.getTeamColor() != getTeamColor()) {
                //promotion capture
                if (nextRow == promotionRow) {
                    moves.add(new ChessMove(myPosition, leftCapturePos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, leftCapturePos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, leftCapturePos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, leftCapturePos, ChessPiece.PieceType.QUEEN));
                }
                //normal capture
                else
                    moves.add(new ChessMove(myPosition, leftCapturePos, null));
            }
        }

        //capture right
        if (nextRow >= 1 && nextRow <= 8 && c + 1 <= 8) {

            ChessPosition rightCapturePos = new ChessPosition(r + direction, c + 1);
            ChessPiece rightCapturePiece = board.getPiece(rightCapturePos);
            if (board.getPiece(rightCapturePos) != null && rightCapturePiece.getTeamColor() != getTeamColor()) {
                //promotion capture
                if (r == promotionRow) {
                    moves.add(new ChessMove(myPosition, rightCapturePos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, rightCapturePos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, rightCapturePos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, rightCapturePos, ChessPiece.PieceType.QUEEN));
                }
                //normal capture
                else
                    moves.add(new ChessMove(myPosition, rightCapturePos, null));
            }
        }

        return moves;

    }
}*/
