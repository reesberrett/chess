package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Moving up
        if (r + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c);
            //potential problem here redefining currentPiece
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving down
        if (r - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving right
        if (c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving right
        if (c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving up and right
        if (r + 1 <= 8 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }

        //Moving up and left
        if (r + 1 <= 8 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving down and right
        if (r - 1 >= 1 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }

        //Moving down and left
        if (r - 1 >= 1 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Castling
        if (c == 5) {

            //Get correct color from GameBoard parameters
            boolean kingMoved = board.getWhiteKingMoved();
            boolean rookLeftMoved = board.getWhiteRookLeftMoved();
            boolean rookRightMoved = board.getWhiteRookRightMoved();

            if (color == ChessGame.TeamColor.BLACK) {
                kingMoved = board.getBlackKingMoved();
                rookLeftMoved = board.getBlackRookLeftMoved();
                rookRightMoved = board.getBlackRookRightMoved();
            }

            //King cannot have already moved
            if (!kingMoved) {
                //Kingside castle: Columns 6 and 7 must be empty
                ChessPosition fPosition = new ChessPosition(r, 6);
                ChessPosition gPosition = new ChessPosition(r, 7);

                if (!rookRightMoved && board.getPiece(fPosition) == null && board.getPiece(gPosition) == null)
                    moves.add(new ChessMove(myPosition, gPosition, null));


                //Queenside castle (Columns 2, 3, and 4 must be empty)
                ChessPosition dPosition = new ChessPosition(r, 4);
                ChessPosition cPosition = new ChessPosition(r, 3);
                ChessPosition bPosition = new ChessPosition(r, 2);

                if (!rookLeftMoved && board.getPiece(dPosition) == null && board.getPiece(cPosition) == null && board.getPiece(bPosition) == null)
                    moves.add(new ChessMove(myPosition, cPosition, null));
            }
        }
        return moves;
    }
}
