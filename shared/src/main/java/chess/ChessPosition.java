package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int rowNum;
    private final int colNum;

    public ChessPosition(int row, int col) {
        this.rowNum = row;
        this.colNum = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return rowNum;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return colNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return rowNum == that.rowNum && colNum == that.colNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, colNum);
    }
}
