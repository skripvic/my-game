package model;

import org.junit.jupiter.api.Test;

import model.*;
import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void straightThree() {
        Field field = new Field(10);
        field.fillField();
        field.changeFieldPiece(0,9, Piece.FORTEST);
        field.changeFieldPiece(1,9, Piece.FORTEST);
        field.changeFieldPiece(2,8, Piece.FORTEST);
        // .  .  .  .
        // .  .  F  .
        // F  F  .  .
        field.update(8*10+2);
        field.update(9*10+2);
        assertNotEquals(field.getPiece(0,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(1,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,9), Piece.FORTEST);
    }

    @Test
    void straightFour() {
        Field field = new Field(10);
        field.fillField();
        field.changeFieldPiece(0,9, Piece.FORTEST);
        field.changeFieldPiece(1,9, Piece.FORTEST);
        field.changeFieldPiece(3,9, Piece.FORTEST);
        field.changeFieldPiece(2,8, Piece.FORTEST);
        // .  .  F  .  .
        // F  F  .  F  .
        field.update(8*10+2);
        field.update(9*10+2);
        assertNotEquals(field.getPiece(0,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(1,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(3,9), Piece.FORTEST);
    }

    @Test
    void angleFive() {
        Field field = new Field(10);
        field.fillField();
        field.changeFieldPiece(0,9, Piece.FORTEST);
        field.changeFieldPiece(1,9, Piece.FORTEST);
        field.changeFieldPiece(3,9, Piece.FORTEST);
        field.changeFieldPiece(2,8, Piece.FORTEST);
        field.changeFieldPiece(2,7, Piece.FORTEST);
        // .  .  F  .  .
        // .  .  F  .  .
        // F  F  .  F  .
        field.update(9*10+3);
        field.update(9*10+2);
        assertNotEquals(field.getPiece(0,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(1,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(3,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,8), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,7), Piece.FORTEST);
    }


    @Test
    void straightFive() {
        Field field = new Field(10);
        field.fillField();
        field.changeFieldPiece(0,9, Piece.FORTEST);
        field.changeFieldPiece(1,9, Piece.FORTEST);
        field.changeFieldPiece(3,9, Piece.FORTEST);
        field.changeFieldPiece(4,9, Piece.FORTEST);
        field.changeFieldPiece(2,8, Piece.FORTEST);
        // T  X  T  X  T . .
        // X  T  F  T  X . .
        // F  F  X  F  F . .
        field.update(9*10+2);
        field.update(8*10+2);
        assertNotEquals(field.getPiece(0,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(1,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(2,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(3,9), Piece.FORTEST);
        assertNotEquals(field.getPiece(4,9), Piece.FORTEST);
    }

    @Test
    void ifRemoved() {
        Field field = new Field(10);
        field.fillField();
        for (int i = 7; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                field.changeFieldPiece(j,i, Piece.FORTEST);
            }
        }
        // F  F  F  . .
        // F  F  F  . .
        // F  F  F  . .
        field.update(8*10+1);
        field.update(8*10+1);
        for (int i = 7; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(field.getPiece(j,i), Piece.FORTEST);
            }
        }
    }

}