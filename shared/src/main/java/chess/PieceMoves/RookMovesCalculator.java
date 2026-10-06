package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        //moving up
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        while (r < 8) {
            r++;

            ChessPosition currentPosition = new ChessPosition(r, c);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
            }
            else {
                if (currentPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                }
                break;
            }
        }

        //moving down
        r = myPosition.getRow();
        while (r > 1) {
            r--;

            ChessPosition currentPosition = new ChessPosition(r, c);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
            }
            else {
                if (currentPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                }
                break;
            }
        }

        //moving right
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (c < 8) {
            c++;

            ChessPosition currentPosition = new ChessPosition(r, c);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
            }
            else {
                if (currentPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                }
                break;
            }
        }

        //moving left
        c = myPosition.getColumn();
        while (c > 1) {
            c--;

            ChessPosition currentPosition = new ChessPosition(r, c);
            currentPiece = board.getPiece(currentPosition);

            if (currentPiece == null) {
                moves.add(new ChessMove(myPosition, currentPosition, null));
            }
            else {
                if (currentPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, currentPosition, null));
                }
                break;
            }
        }

        return moves;
    }
}
