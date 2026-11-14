package client;

import chess.*;
import static ui.EscapeSequences.*;

public class BoardDrawer {

    public static void drawBoard(ChessBoard board, boolean whiteView) {
        StringBuilder out = new StringBuilder("\n");
        drawHeaders(out, whiteView);

        for (int i = 0; i < 8; i++) {
            int row = whiteView ? (8 - i) : (i + 1);
            drawRow(out, board, row, whiteView);
        }

        drawHeaders(out, whiteView);
        out.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        System.out.println(out);
    }

    private static void drawHeaders(StringBuilder out, boolean whiteView) {
        out.append(SET_BG_COLOR_LIGHT_GREY).append(SET_TEXT_COLOR_BLACK).append("   ");
        String[] cols = whiteView
                ? new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}
                : new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        for (String col : cols) {
            out.append(" ").append(col).append("\u2003");
        }
        out.append("   ").append(RESET_BG_COLOR).append("\n");
    }

    private static void drawRow(StringBuilder out, ChessBoard board, int row, boolean whiteView) {
        out.append(SET_BG_COLOR_LIGHT_GREY).append(SET_TEXT_COLOR_BLACK).append(" ").append(row).append(" ");

        for (int j = 0; j < 8; j++) {
            int col = whiteView ? (j + 1) : (8 - j);
            boolean isLightSquare = (row + col) % 2 != 0;
            out.append(isLightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_DARK_GREEN);

            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            out.append(getPieceSymbol(piece));
        }

        out.append(SET_BG_COLOR_LIGHT_GREY).append(SET_TEXT_COLOR_BLACK).append(" ").append(row).append(" ");
        out.append(RESET_BG_COLOR).append("\n");
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        String colorCode = isWhite ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
        return colorCode + switch (piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
