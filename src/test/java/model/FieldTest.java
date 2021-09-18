package model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private final Field field = new Field(10);
    private String fieldString;
    private int updateNum;

    FieldListener testListener = new FieldListener() {
        @Override
        public void update() {
            updateNum++;
            if (updateNum <= 2){
                fieldString = field.toString();
            }
        }

        @Override
        public void updateScore(int score) { }
    };

    private int getIndex(int i, int j){
        return i*10+j;
    }


    @Test
    void straightThree() {
        field.setListener(testListener);
        field.fillField();
        updateNum = 0;
        field.changeFieldPiece(0,9, Piece.CROSS);
        field.changeFieldPiece(1,9, Piece.CROSS);
        field.changeFieldPiece(2,8, Piece.CROSS);
        // .  .  .  .
        // .  .  X  .
        // X  X  .  .
        field.update(8*10+2);
        field.update(9*10+2);
        assertEquals(" ", fieldString.substring(9, 10));
        assertEquals(" ", fieldString.substring(19, 20));
        assertEquals(" ", fieldString.substring(29, 30));
    }

    @Test
    void straightFour() {
        field.setListener(testListener);
        field.fillField();
        updateNum = 0;
        field.changeFieldPiece(0,9, Piece.CROSS);
        field.changeFieldPiece(1,9, Piece.CROSS);
        field.changeFieldPiece(3,9, Piece.CROSS);
        field.changeFieldPiece(2,8, Piece.CROSS);
        // .  .  .  .  .
        // .  .  X  .  .
        // X  X  .  X  .
        field.update(8*10+2);
        field.update(9*10+2);
        assertEquals(" ", fieldString.substring(getIndex(0,9), getIndex(0,9)+1) );
        assertEquals(" ", fieldString.substring(getIndex(1,9), getIndex(1,9)+1) );
        assertEquals(" ", fieldString.substring(getIndex(2,9), getIndex(2,9)+1) );
        assertEquals(" ", fieldString.substring(getIndex(3,9), getIndex(3,9)+1) );

    }

    @Test
    void angleFive() {
        field.setListener(testListener);
        field.fillField();
        updateNum = 0;
        field.changeFieldPiece(0,9, Piece.CROSS);
        field.changeFieldPiece(1,9, Piece.CROSS);
        field.changeFieldPiece(3,9, Piece.CROSS);
        field.changeFieldPiece(2,8, Piece.CROSS);
        field.changeFieldPiece(2,7, Piece.CROSS);
        // .  .  .  .  .
        // .  .  X  .  .
        // .  .  X  .  .
        // X  X  .  X  .
        field.update(9*10+3);
        field.update(9*10+2);
        System.out.println(fieldString);
        assertEquals(" ", fieldString.substring(getIndex(0,9), getIndex(0,9)+1));
        assertEquals(" ", fieldString.substring(getIndex(1,9), getIndex(1,9)+1));
        assertEquals(" ", fieldString.substring(getIndex(2,9), getIndex(2,9)+1));
        assertEquals(" ", fieldString.substring(getIndex(2,8), getIndex(2,8)+1));
        assertEquals(" ", fieldString.substring(getIndex(2,7), getIndex(2,7)+1));
    }


    @Test
    void straightFive() {
        field.setListener(testListener);
        field.fillField();
        updateNum = 0;
        field.changeFieldPiece(0,9, Piece.CROSS);
        field.changeFieldPiece(1,9, Piece.CROSS);
        field.changeFieldPiece(3,9, Piece.CROSS);
        field.changeFieldPiece(4,9, Piece.CROSS);
        field.changeFieldPiece(2,8, Piece.CROSS);
        // .  .  .  .  .  .
        // .  .  X  .  .  .
        // X  X  .  X  X  .
        field.update(9*10+2);
        field.update(8*10+2);
        assertEquals( " ", fieldString.substring(getIndex(0,9), getIndex(0,9)+1));
        assertEquals( " ", fieldString.substring(getIndex(1,9), getIndex(1,9)+1));
        assertEquals( " ", fieldString.substring(getIndex(2,9), getIndex(2,9)+1));
        assertEquals( " ", fieldString.substring(getIndex(3,9), getIndex(3,9)+1));
        assertEquals( " ", fieldString.substring(getIndex(4,9), getIndex(4,9)+1));
    }

    @Test
    void ifRemoved() {
        field.setListener(testListener);
        field.fillField();
        updateNum = 0;
        field.changeFieldPiece(0, 9, Piece.CROSS);
        field.changeFieldPiece(1, 9, Piece.CROSS);
        field.changeFieldPiece(2, 9, Piece.CROSS);
        System.out.println(fieldString);
        // .  .  .  .  .
        // X  X  X  .  .
        field.update(9*10+1);
        field.update(9*10+1);
        fieldString = field.toString(); //this test doesn't get to listener.update
        assertEquals("X", fieldString.substring(getIndex(0,9), getIndex(0,9)+1));
        assertEquals("X", fieldString.substring(getIndex(1,9), getIndex(1,9)+1));
        assertEquals("X", fieldString.substring(getIndex(2,9), getIndex(2,9)+1));
    }

}