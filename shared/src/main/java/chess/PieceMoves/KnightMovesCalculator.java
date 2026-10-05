package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        //moving left 2 and up 1
        if (r + 1 <= 8 && c - 2 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c - 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }
        //moving left 2 and down 1
        if (r - 1 >= 1 && c - 2 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c - 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }
        //moving up 2 and left 1
        if (r + 2 <= 8 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r + 2, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }
        //moving up 2 and right 1
        if (r + 2 <= 8 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 2, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }
        //moving right 2 and up 1
        if (r + 1 <= 8 && c + 2 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r + 1, c + 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }
        //moving right 2 and down 1
        if (r - 1 >= 1 && c + 2 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r - 1, c + 2);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }
        //moving down 2 and right 1
        if (r - 2 >= 1 && c + 1 <= 8) {

            ChessPosition currentPosition = new ChessPosition(r - 2, c + 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));

        }
        //moving down 2 and left 1
        if (r - 2 >= 1 && c - 1 >= 1) {

            ChessPosition currentPosition = new ChessPosition(r - 2, c - 1);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null || currentPiece.getTeamColor() != color)
                moves.add(new ChessMove(myPosition, currentPosition, null));
        }

        return moves;
    }
}
