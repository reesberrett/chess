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

            ChessPiece promotionPiece = piece;
            if (move.getPromotionPiece() != null)
                promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());


            //Simulate move
            board.addPiece(endPosition, promotionPiece);
            board.addPiece(startPosition, null);

            if (!isInCheck(piece.getTeamColor()))
                legalMoves.add(move);

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

        if (move.getPromotionPiece() != null) {
            promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }


        //Update piece at new position
        board.addPiece(endPosition, promotionPiece);
        //Delete piece at old position
        board.addPiece(startPosition, null);

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
