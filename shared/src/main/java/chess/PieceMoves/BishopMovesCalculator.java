package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements MoveCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPiece currentPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = currentPiece.getTeamColor();

        //moving up to the right
        int r = myPosition.getRow();
        int c = myPosition.getColumn();
        while (r < 8 && c < 8) {
            r++;
            c++;

            ChessPosition nextPosition = new ChessPosition(r, c);
            ChessPiece nextPiece = board.getPiece(nextPosition);

            if (nextPiece == null) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
            }
            else {
                if (nextPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
                break;
            }
        }

        //moving up to the left
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (r < 8 && c > 1) {
            r++;
            c--;

            ChessPosition nextPosition = new ChessPosition(r, c);
            ChessPiece nextPiece = board.getPiece(nextPosition);

            if (nextPiece == null) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
            }
            else {
                if (nextPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
                break;
            }
        }

        //moving down to the left
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (r > 1 && c < 8) {
            r--;
            c++;

            ChessPosition nextPosition = new ChessPosition(r, c);
            ChessPiece nextPiece = board.getPiece(nextPosition);

            if (nextPiece == null) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
            }
            else {
                if (nextPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
                break;
            }
        }

        //moving down to the right
        r = myPosition.getRow();
        c = myPosition.getColumn();
        while (r > 1 && c > 1) {
            r--;
            c--;

            ChessPosition nextPosition = new ChessPosition(r, c);
            ChessPiece nextPiece = board.getPiece(nextPosition);

            if (nextPiece == null) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
            }
            else {
                if (nextPiece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
                break;
            }
        }

        return moves;
    }
}
