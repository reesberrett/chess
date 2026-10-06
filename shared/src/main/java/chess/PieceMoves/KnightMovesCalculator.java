package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Moving left 2 and up 1
        if (r + 1 <= 8 && c - 2 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c - 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving left 2 and down 1
        if (r - 1 >= 1 && c - 2 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c - 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving up 2 and left 1
        if (r + 2 <= 8 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r + 2, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving up 2 and right 1
        if (r + 2 <= 8 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 2, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving right 2 and up 1
        if (r + 1 <= 8 && c + 2 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c + 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }

        //Moving right 2 and down 1
        if (r - 1 >= 1 && c + 2 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c + 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        //Moving down 2 and right 1
        if (r - 2 >= 1 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r - 2, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }

        //Moving down 2 and left 1
        if (r - 2 >= 1 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 2, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        return moves;
    }
}
