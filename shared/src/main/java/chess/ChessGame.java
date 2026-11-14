package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor currentTurnColor;

    public ChessGame() {
        board.resetBoard();
        currentTurnColor = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurnColor;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurnColor = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTurnColor == chessGame.currentTurnColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurnColor);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece originalPiece = board.getPiece(startPosition);
        if (originalPiece == null) {
            return Collections.emptyList();
        }

        Collection<ChessMove> legalMoves = new ArrayList<>();
        TeamColor color = originalPiece.getTeamColor();
        boolean isKing = (originalPiece.getPieceType() == ChessPiece.PieceType.KING);

        for (ChessMove move : originalPiece.pieceMoves(board, startPosition)) {
            ChessPosition endPosition = move.endPosition();

            //Validate Castling before modifying the board
            if (isKing && Math.abs(endPosition.getColumn() - startPosition.getColumn()) == 2
                    && !isCastleLegal(startPosition, endPosition, originalPiece)) {
                continue;
            }


            //Determine moving piece
            ChessPiece movingPiece = (move.promotionPiece() != null)
                    ? new ChessPiece(color, move.promotionPiece())
                    : originalPiece;

            //Simulate move
            ChessPiece capturedPiece = board.getPiece(endPosition);
            ChessPosition epPawnPos = getEnPassantCapturePos(originalPiece, endPosition);
            ChessPiece epPawn = (epPawnPos != null) ? board.getPiece(epPawnPos) : null;

            board.addPiece(endPosition, movingPiece);
            board.addPiece(startPosition, null);
            if (epPawnPos != null) {
                board.addPiece(epPawnPos, null);
            }

            //Check if King is safe
            if (!isInCheck(color)) {
                legalMoves.add(move);
            }

            //Undo simulation
            board.addPiece(startPosition, originalPiece);
            board.addPiece(endPosition, capturedPiece);
            if (epPawnPos != null) {
                board.addPiece(epPawnPos, epPawn);
            }
        }

        return legalMoves;
    }

    private boolean isCastleLegal(ChessPosition start, ChessPosition end, ChessPiece king) {
        TeamColor color = king.getTeamColor();
        if (isInCheck(color)) {
            return false; // Cannot castle out of check
        }

        // Check the passing square (col 6 for kingside, col 4 for queenside)
        int passingCol = (end.getColumn() == 7) ? 6 : 4;
        ChessPosition passingPos = new ChessPosition(start.getRow(), passingCol);

        board.addPiece(passingPos, king);
        board.addPiece(start, null);
        boolean passesThroughCheck = isInCheck(color);

        // Undo passing square test
        board.addPiece(start, king);
        board.addPiece(passingPos, null);

        return !passesThroughCheck;
    }

    private ChessPosition getEnPassantCapturePos(ChessPiece piece, ChessPosition endPosition) {
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN
                && endPosition.equals(board.getEnPassantPosition())) {
            int captureRow = (piece.getTeamColor() == TeamColor.WHITE) ? 5 : 4;
            return new ChessPosition(captureRow, endPosition.getColumn());
        }
        return null;
    }


    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.startPosition();
        ChessPosition end = move.endPosition();
        ChessPiece piece = board.getPiece(start);

        //Validate turn and move legality
        if (piece == null || piece.getTeamColor() != getTeamTurn() || !validMoves(start).contains(move)) {
            throw new InvalidMoveException("Illegal move!");
        }

        //Handle special move side effects
        updateCastlingFlags(piece, start);
        handleCastlingRookMove(piece, start, end);
        handleEnPassant(piece, start, end);

        //Move piece, handle promotion case
        ChessPiece finalPiece = (move.promotionPiece() != null)
                ? new ChessPiece(piece.getTeamColor(), move.promotionPiece())
                : piece;

        board.addPiece(end, finalPiece);
        board.addPiece(start, null);

        //Switch turn
        setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    private void updateCastlingFlags(ChessPiece piece, ChessPosition start) {
        boolean isWhite = (piece.getTeamColor() == TeamColor.WHITE);

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (isWhite) {
                board.setWhiteKingMoved(true);
            }
            else {
                board.setBlackKingMoved(true);
            }
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (start.getColumn() == 1) {
                if (isWhite) {
                    board.setWhiteRookLeftMoved(true);
                }
                else {
                    board.setBlackRookLeftMoved(true);
                }
            } else if (start.getColumn() == 8) {
                if (isWhite) {
                    board.setWhiteRookRightMoved(true);
                }
                else {
                    board.setBlackRookRightMoved(true);
                }
            }
        }
    }

    private void handleCastlingRookMove(ChessPiece piece, ChessPosition start, ChessPosition end) {
        if (piece.getPieceType() != ChessPiece.PieceType.KING || Math.abs(end.getColumn() - start.getColumn()) != 2) {
            return;
        }

        int row = end.getRow();
        boolean isKingside = (end.getColumn() == 7);
        ChessPosition rookStart = new ChessPosition(row, isKingside ? 8 : 1);
        ChessPosition rookEnd = new ChessPosition(row, isKingside ? 6 : 4);

        board.addPiece(rookEnd, board.getPiece(rookStart));
        board.addPiece(rookStart, null);
    }

    private void handleEnPassant(ChessPiece piece, ChessPosition start, ChessPosition end) {
        ChessPosition previousEP = board.getEnPassantPosition();
        ChessBoard.setEnPassantPosition(null); // Consider making this an instance method on board

        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return;
        }

        //Remove captured pawn if this move executed an en passant capture
        if (end.equals(previousEP)) {
            int capturedRow = (piece.getTeamColor() == TeamColor.WHITE) ? 5 : 4;
            board.addPiece(new ChessPosition(capturedRow, end.getColumn()), null);
        }

        //Record new en passant target square if pawn pushed 2 spaces
        if (Math.abs(end.getRow() - start.getRow()) == 2) {
            int epRow = (start.getRow() + end.getRow()) / 2; // 3 for White, 6 for Black
            ChessBoard.setEnPassantPosition(new ChessPosition(epRow, start.getColumn()));
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null) {
            return false;
        }

        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(pos);

                if (piece == null || piece.getTeamColor() == teamColor) {
                    continue;
                }
                if (canAttack(piece, pos, kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canAttack(ChessPiece piece, ChessPosition from, ChessPosition target) {
        for (ChessMove move : piece.pieceMoves(board, from)) {
            if (move.endPosition().equals(target)) {
                return true;
            }
        }
        return false;
    }


    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return pos;
                }
            }
        }
        return null;
    }



    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //Must be in check
        if (!isInCheck(teamColor)) {
            return false;
        }

        //No piece must have valid moves

        //Look for same-colored pieces
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPosition = new ChessPosition(r, c);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                //If there is a same-colored piece, check if it has any legal moves
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> legalMoves = validMoves(currentPosition);

                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        //There are still legal moves: not checkmate
                        return false;
                    }
                }
            }
        }
        //There are no more legal moves: checkmate
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Cannot be in check
        if (isInCheck(teamColor)) {
            return false;
        }

        //No piece must have valid moves
        //Look for same-colored pieces
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPosition = new ChessPosition(r, c);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                //If there is a same-colored piece, check if it has any legal moves
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> legalMoves = validMoves(currentPosition);

                    if (legalMoves != null && !legalMoves.isEmpty()) {
                        //There are still legal moves: not stalemate
                        return false;
                    }
                }
            }
        }
        //There are no more legal moves: stalemate
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
