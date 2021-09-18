package model;

public enum Piece {
    DIAMOND("D"),
    TRIANGLE("T"),
    SQUARE("S"),
    CIRCLE("O"),
    CROSS("X"),
    EMPTY(" ");

    private final String symbol;

    Piece(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
