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
        field[9][0] = Piece.CIRCLE;
        field[9][1] = Piece.CIRCLE;
        field[8][2] = Piece.CIRCLE;
        field[7][2] = Piece.CIRCLE;
        field[9][3] = Piece.CIRCLE;
        listener.update();
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
        listener.update();
        if (!removeCombinations()){
            switchPieces(x1, y1, x2, y2);
            listener.update();
        }
    }

    private void dropGeneratePieces(){
//        int countDeleted = 0;
//        int k;
//        listener.update();
//        int iMin = Math.min(x1, x2) - 2;
//        int iMax = Math.min(x1, x2) + 4;
//        if (iMin < 0) iMin = 0;
//        if (iMax > size-1) iMax = size;
//        for (int j = size-1; j >= 0; j--) {
//            for (int i = iMin; i < iMax; i++) {
//                if (field[i][j] == Piece.EMPTY) {
//                    k = j - 1;
//                    while (k >= 0) {
//                        if (field[i][k] == Piece.EMPTY){
//                            k--;
//                        }
//                        else {
//                            switchPieces(i, j, i, k);
//                            listener.update();
//                            countDeleted += checkForCombinations(i, j);
//                            if (countDeleted > 0) countDeleted += movePiecesFromTop(i, j);
//                            break;
//                        }
//                    }
//                    if (k == -1){
//                        generatePiece(i, j);
//                        listener.update();
//                        countDeleted += checkForCombinations(i, j);
//                        if (countDeleted > 0) countDeleted += movePiecesFromTop(i, j);
//                    }
//                }
//            }
//            System.out.println(pointSum);
//            listener.update();
//        }
        //return countDeleted;
        listener.update();
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
                    System.out.println(lowestEmpty);
                    field[i][j] = Piece.EMPTY;
                }
            }
            listener.update();
            for (; lowestEmpty >= 0; lowestEmpty--){
                System.out.println(lowestEmpty);
                generatePiece(i, lowestEmpty);
            }
            listener.update();
        }
        System.out.println("drop");
        removeCombinations();
    }

    private void switchPieces(int x1, int y1, int x2, int y2){
        Piece tmp = field[x1][y1];
        field[x1][y1] = field[x2][y2];
        field[x2][y2] = tmp;
    }

    private boolean removeCombinations(){
//        int lineSize = 5;
//        int countDeleted = 0;
//        boolean[] xEquals = new boolean[lineSize];
//        boolean[] yEquals = new boolean[lineSize];
//        for (int i = 0; i < lineSize; i++){
//            if (x-2+i >= 0 && x-2+i < size && field[x][y] == field[x-2+i][y]){
//                xEquals[i] = true;
//            }
//        }
//        for (int i = 0; i < lineSize; i++){
//            if (y-2+i >= 0 && y-2+i < size && field[x][y] == field[x][y-2+i])
//                yEquals[i] = true;
//        }
//        if (xEquals[0] & xEquals[1]){
//            if (!xEquals[3] & xEquals[4])
//                xEquals[4] = false;
//            for (int i = 0; i < lineSize; i++)
//                if (xEquals[i]) {
//                    field[x-2+i][y] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        } else if (xEquals[3] & xEquals[4]){
//            if (!xEquals[1] & xEquals[0])
//                xEquals[0] = false;
//            for (int i = 0; i < lineSize; i++)
//                if (xEquals[i]) {
//                    field[x-2+i][y] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        } else if (xEquals[1] & xEquals[3]){
//            for (int i = 0; i < lineSize; i++)
//                if (xEquals[i]) {
//                    field[x-2+i][y] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        }
//        if (yEquals[0] & yEquals[1]){
//            if (!yEquals[3] & yEquals[4])
//                yEquals[4] = false;
//            for (int i = 0; i < lineSize; i++)
//                if (yEquals[i]) {
//                    field[x][y-2+i] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        } else if (yEquals[3] & yEquals[4]){
//            if (!yEquals[1] & yEquals[0])
//                yEquals[0] = false;
//            for (int i = 0; i < lineSize; i++)
//                if (yEquals[i]) {
//                    field[x][y-2+i] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        } else if (yEquals[1] & yEquals[3]){
//            for (int i = 0; i < lineSize; i++)
//                if (yEquals[i]) {
//                    field[x][y-2+i] = Piece.EMPTY;
//                    countDeleted++;
//                }
//        }
//        return countDeleted;
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
                        listener.updateScore(pointSum);
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
                        listener.updateScore(pointSum);
                    }
                    curPiece = field[i][j];
                    curCombSize = 1;
                }
            }
        }
        listener.update();
        System.out.println("delete");
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
