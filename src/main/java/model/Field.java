package model;

import java.util.Random;

public class Field {
    private final int size;
    private int pressedPiece1, pressedPiece2;
    private final Piece[][] field;
    private int pointSum;
    private FieldListener listener;
    private final Random rand = new Random();

    public Field(int size) {
        this.size = size;
        this.field = new Piece[size][size];
    }

    public void fillField(){
        this.pressedPiece1 = -1;
        this.pressedPiece2 = -1;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                generatePiece(i, j);
            }
        }
        if (listener != null) listener.update();
    }

    private void generatePiece(int i, int j) {
        int randomNum = rand.nextInt(5);
        Piece newPiece;
        while (true) {
            switch (randomNum) {
                case (0) -> newPiece = Piece.DIAMOND;
                case (1) -> newPiece = Piece.TRIANGLE;
                case (2) -> newPiece = Piece.SQUARE;
                case (3) -> newPiece = Piece.CIRCLE;
                case (4) -> newPiece = Piece.CROSS;
                default -> newPiece = Piece.EMPTY;
            }
            if ((i >= 2) && (j >= 2) && (field[i - 2][j] == newPiece && field[i - 1][j] == newPiece
                    || field[i][j - 2] == newPiece && field[i][j - 1] == newPiece)
                    || i < 2 && j >= 2 && field[i][j - 2] == newPiece && field[i][j - 1] == newPiece
                    || i >= 2 && j < 2 && field[i - 2][j] == newPiece && field[i - 1][j] == newPiece) {
                randomNum = (randomNum + 1) % 5;
            } else {
                field[i][j] = newPiece;
                break;
            }
        }
    }

    public void setListener(FieldListener listener) {
        this.listener = listener;
    }

    public void update(int pieceNum) {
        if (pressedPiece1 == -1)
            pressedPiece1 = pieceNum;
        else {
            pressedPiece2 = pieceNum;
            int x1 = pressedPiece1 % size;
            int y1 = pressedPiece1 / size;
            int x2 = pressedPiece2 % size;
            int y2 = pressedPiece2 / size;
            if ((x1 - x2 == -1 || x1 - x2 == 1) && y1 - y2 == 0 || (y1 - y2 == -1 || y1 - y2 == 1) && x1 - x2 == 0) {
                tryMovePieces(x1, y1, x2, y2);
            }
            pressedPiece1 = -1;
            pressedPiece2 = -1;
        }
    }

    private void tryMovePieces(int x1, int y1, int x2, int y2){
        switchPieces(x1, y1, x2, y2);
        if (listener != null) listener.update();
        if (!removeCombinations()){
            switchPieces(x1, y1, x2, y2);
            if (listener != null) listener.update();
        }
    }

    private void dropGeneratePieces(){
        if (listener != null) listener.update();
        int countEmpty, lowestEmpty = 0;
        for (int i = 0; i < size; i++) {
            countEmpty = 0;
            for (int j = size-1; j >= 0; j--) {
                if (field[i][j] == Piece.EMPTY) {
                    countEmpty++;
                    if (countEmpty == 1) lowestEmpty = j;
                }
                if (field[i][j] != Piece.EMPTY && countEmpty > 0){
                    field[i][lowestEmpty] = field[i][j];
                    lowestEmpty--;
                    field[i][j] = Piece.EMPTY;
                }
            }
            if (listener != null) listener.update();
            for (; lowestEmpty >= 0; lowestEmpty--){
                generatePiece(i, lowestEmpty);
            }
            if (listener != null) listener.update();
        }
        removeCombinations();
    }

    private void switchPieces(int x1, int y1, int x2, int y2){
        Piece tmp = field[x1][y1];
        field[x1][y1] = field[x2][y2];
        field[x2][y2] = tmp;
    }

    private boolean removeCombinations(){
        int curCombSize, additionalSize;
        boolean isRemoved = false;
        Piece curPiece = Piece.EMPTY;
        for (int j = 0; j < size; j++) {
            curCombSize = 0;
            for (int i = 0; i < size; i++) {
                if (i == 0) curPiece = field[i][j];
                if (field[i][j] == Piece.EMPTY) continue;
                if (field[i][j] == curPiece) curCombSize++;
                if (field[i][j] != curPiece || i == size-1){
                    if (curCombSize > 2){
                        isRemoved = true;
                        additionalSize = 0;
                        int newI = 0;
                        int addForLastPiece = 0;
                        if (i == size-1 && field[i][j] == curPiece) {
                            addForLastPiece++;
                        }
                        for (int k = 0; k < curCombSize; k++) {
                            newI = i-curCombSize+k+addForLastPiece;
                            if (j != 0 && field[newI][j-1] == curPiece && j != size-1 && field[newI][j+1] == curPiece){
                                additionalSize += 2;
                                field[newI][j-1] = Piece.EMPTY;
                                field[newI][j+1] = Piece.EMPTY;
                                if (j != 1 && field[newI][j-2] == curPiece) {
                                    additionalSize++;
                                    field[newI][j-2] = Piece.EMPTY;
                                }
                                if (j != size-2 && field[newI][j+2] == curPiece) {
                                    additionalSize++;
                                    field[newI][j+2] = Piece.EMPTY;
                                }
                            }
                            if (j != 0 && field[newI][j-1] == curPiece && j != 1 && field[newI][j-2] == curPiece){
                                additionalSize += 2;
                                field[newI][j-1] = Piece.EMPTY;
                                field[newI][j-2] = Piece.EMPTY;
                            }
                            if (j != size-1 && field[newI][j+1] == curPiece && j != size-2 && field[newI][j+2] == curPiece){
                                additionalSize += 2;
                                field[newI][j+1] = Piece.EMPTY;
                                field[newI][j+2] = Piece.EMPTY;
                            }
                            field[newI][j] = Piece.EMPTY;
                            if (additionalSize != 0) break;
                        }
                        for (; newI < i-1+addForLastPiece; newI++)
                            field[newI][j] = Piece.EMPTY;
                        curCombSize += additionalSize;
                        if (curCombSize == 5) pointSum += 7;
                        else if (curCombSize == 6) pointSum += 10;
                        else if (curCombSize == 7) pointSum += 15;
                        else pointSum += curCombSize;
                        if (listener != null) listener.updateScore(pointSum);
                    }
                    curPiece = field[i][j];
                    curCombSize = 1;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            curCombSize = 0;
            for (int j = 0; j < size; j++) {
                if (j == 0) curPiece = field[i][j];
                if (field[i][j] == curPiece && field[i][j] != Piece.EMPTY) curCombSize++;
                if (field[i][j] != curPiece || j == size-1) {
                    if (curCombSize > 2){
                        isRemoved = true;
                        int addForLastPiece = 0;
                        if (j == size-1 && field[i][j] == curPiece) addForLastPiece++;
                        for (int k = 0; k < curCombSize; k++){
                            field[i][j-curCombSize+k+addForLastPiece] = Piece.EMPTY;
                            pointSum += curCombSize;
                        }
                        if (listener != null) listener.updateScore(pointSum);
                    }
                    curPiece = field[i][j];
                    curCombSize = 1;
                }
            }
        }
        if (listener != null) listener.update();
        if (isRemoved) dropGeneratePieces();
        return isRemoved;
    }


    public void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = Piece.EMPTY;
            }
        }
        this.pointSum = 0;
        listener.updateScore(pointSum);
    }

    public int getScore(){
        return pointSum;
    }

    public void changeFieldPiece(int i, int j, Piece piece){
        field[i][j] = piece;
    }

    public Piece getPiece(int i, int j){
        return field[i][j];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(field[i][j].toString());
            }
        }
        return builder.toString();
    }

}
