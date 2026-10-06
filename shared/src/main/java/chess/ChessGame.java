package chess;

import java.util.ArrayList;
import java.util.Collection;
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
        Collection<ChessMove> legalMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);

        //Return null if no piece at startPosition
        if (piece == null)
            return null;

        Collection<ChessMove> basicMoves = piece.pieceMoves(board, startPosition);

        //Simulate each move to ensure a move doesnt leave someone in check
        for (ChessMove move : basicMoves) {
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece targetPiece = board.getPiece(endPosition);

            //Simulate possible promotion
            ChessPiece promotionPiece = piece;
            if (move.getPromotionPiece() != null)
                promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());


            //Simulate move
            board.addPiece(endPosition, promotionPiece);
            board.addPiece(startPosition, null);

            boolean legal = !isInCheck(piece.getTeamColor());
            boolean isCastleKingside = false;
            boolean isCastleQueenside = false;
            int row = endPosition.getRow();

            if (piece.getPieceType() == ChessPiece.PieceType.KING && startPosition.getColumn() == 5) {

                //Check to see if king was under attack before moving
                board.addPiece(startPosition, piece);
                board.addPiece(endPosition, targetPiece);
                boolean startedInCheck = isInCheck(piece.getTeamColor());

                //Undo simulated move
                board.addPiece(endPosition, promotionPiece);
                board.addPiece(startPosition, null);

                //Cannot castle out of check
                if (startedInCheck) {
                    legal = false;
                }

                //Check that each square in castling will not put king in check
                if (legal) {
                    if (endPosition.getColumn() == 7) {
                        //Simulate passing square
                        board.addPiece(new ChessPosition(row, 6), piece);
                        board.addPiece(endPosition, null);

                        //Cannot castle into check
                        if (isInCheck(piece.getTeamColor()))
                            legal = false;

                        //Undo simulated move
                        board.addPiece(endPosition, piece);
                        board.addPiece(new ChessPosition(row, 6), null);

                        //If no checks, castle
                        if (legal) {
                            isCastleKingside = true;
                            board.addPiece(new ChessPosition(row, 6), board.getPiece(new ChessPosition(row, 8)));
                            board.addPiece(new ChessPosition(row, 8), null);
                        }

                    } else if (endPosition.getColumn() == 3) {
                        //Simulate passing square
                        board.addPiece(new ChessPosition(row, 4), piece);
                        board.addPiece(endPosition, null);

                        //Cannot castle into check
                        if (isInCheck(piece.getTeamColor()))
                            legal = false;

                        //Undo simulated move
                        board.addPiece(endPosition, piece);
                        board.addPiece(new ChessPosition(row, 4), null);

                        //If no checks, castle
                        if (legal) {
                            isCastleQueenside = true;
                            board.addPiece(new ChessPosition(row, 4), board.getPiece(new ChessPosition(row, 1)));
                            board.addPiece(new ChessPosition(row, 1), null);
                        }
                    }
                }
            }
            //Add move to legal moves
            if (legal)
                legalMoves.add(move);

            if (isCastleKingside) {
                board.addPiece(new ChessPosition(row, 8), board.getPiece(new ChessPosition(row, 6)));
                board.addPiece(new ChessPosition(row, 6), null);
            } else if (isCastleQueenside) {
                board.addPiece(new ChessPosition(row, 1), board.getPiece(new ChessPosition(row, 4)));
                board.addPiece(new ChessPosition(row, 4), null);
            }

            //Undo move
            board.addPiece(startPosition, piece);
            board.addPiece(endPosition, targetPiece);
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece promotionPiece = piece;

        //Move must be a piece at the starting position and must be the current players color
        if (piece == null || piece.getTeamColor() != getTeamTurn())
            throw new InvalidMoveException("Cannot perform move");

        //Move must be in piece's validMoves()
        Collection<ChessMove> legalMoves = validMoves(startPosition);
        if (!legalMoves.contains(move))
            throw new InvalidMoveException("Illegal move for current piece!");

        //Case for promotions
        if (move.getPromotionPiece() != null)
            promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());

        //Case for castling
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == TeamColor.WHITE)
                board.whiteKingMoved = true;
            else
                board.blackKingMoved = true;
        }

        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                if (startPosition.getColumn() == 1)
                    board.whiteRookLeftMoved = true;
                if (startPosition.getColumn() == 8)
                    board.whiteRookRightMoved = true;
            } else {
                if (startPosition.getColumn() == 1)
                    board.blackRookLeftMoved = true;
                if (startPosition.getColumn() == 8)
                    board.blackRookRightMoved = true;
            }
        }

        //Update piece at new position
        board.addPiece(endPosition, promotionPiece);
        //Delete piece at old position
        board.addPiece(startPosition, null);

        //Castling: moving rook in addition to king
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            int row = endPosition.getRow();
            int startCol = startPosition.getColumn();
            int endCol = endPosition.getColumn();

            // Kingside Castle: King moved 2 spaces right (5 to 7)
            if (startCol == 5 && endCol == 7) {
                ChessPosition rookStart = new ChessPosition(row, 8);
                ChessPosition rookEnd = new ChessPosition(row, 6);
                board.addPiece(rookEnd, board.getPiece(rookStart));
                board.addPiece(rookStart, null);
            }
            // Queenside Castle: King moved 2 spaces left (5 to 3)
            else if (startCol == 5 && endCol == 3) {
                ChessPosition rookStart = new ChessPosition(row, 1);
                ChessPosition rookEnd = new ChessPosition(row, 4);
                board.addPiece(rookEnd, board.getPiece(rookStart));
                board.addPiece(rookStart, null);
            }
        }

        //Swap colors
        if (currentTurnColor == TeamColor.WHITE)
            setTeamTurn(TeamColor.BLACK);
        else
            setTeamTurn(TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPosition = null;

        //Find location of king of given color
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPosition = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(currentPosition);

                //Save king coordinates
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = currentPosition;
                    break;
                }
            }
            //Break out of all loops (this one got me for a while lol)
            if (kingPosition != null)
                break;
        }

        //Scan the board for enemy pieces
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition currentPosition = new ChessPosition(r, c);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {

                    //Find all possible moves of each enemy piece found
                    Collection<ChessMove> enemyMoves = currentPiece.pieceMoves(board, currentPosition);

                    for (ChessMove move : enemyMoves) {
                        //Check if enemy moves target king
                        ChessPosition targetPosition = move.getEndPosition();

                        if (targetPosition.getRow() == kingPosition.getRow() && targetPosition.getColumn() == kingPosition.getColumn()) {
                            //king is under attack: check
                            return true;
                        }
                    }
                }
            }
        }
        //king is under no attacks: not check
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //Must be in check
        if (!isInCheck(teamColor))
            return false;

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
        if (isInCheck(teamColor))
            return false;

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
