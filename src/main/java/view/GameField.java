package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GameField extends JPanel {
    private static final double PIECE_FRACTION = 0.75;
    private static final int LINE_WIDTH = 5;
    private final int fieldSize;
    private String field;
    private ClickListener listener = null;
    private boolean isPieceOdd = false;
    private int pieceHighlightedX;
    private int pieceHighlightedY;

    public GameField(int fieldSize) {
        this.fieldSize = fieldSize;
        this.field = "-".repeat(fieldSize * fieldSize);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (listener != null) {
                    if (!isPieceOdd) {
                        pieceHighlightedX = (int) event.getPoint().getX()/squareWidth();
                        pieceHighlightedY = (int) event.getPoint().getY()/squareHeight();
                        pieceHighlight(pieceHighlightedX, pieceHighlightedY);
                        isPieceOdd = true;
                    } else {
                        isPieceOdd = false;
                        pieceHighlight((int) event.getPoint().getX()/squareWidth(), (int) event.getPoint().getY()/squareHeight());
                        pieceUnhighlight(pieceHighlightedX, pieceHighlightedY);
                        pieceUnhighlight((int) event.getPoint().getX()/squareWidth(), (int) event.getPoint().getY()/squareHeight());
                    }
                    listener.onSquareClick(squareNum((int) event.getPoint().getX(), (int) event.getPoint().getY()));
                }
            }
        });
    }

    private int squareNum(int x, int y) {
        return y / squareHeight() * fieldSize + x / squareWidth();
    }

    private int squareWidth() { return (int) getSize().getWidth() / fieldSize; }

    private int squareHeight() { return (int) getSize().getHeight() / fieldSize; }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                drawSquare(g, i, j);
            }
        }
        updateField(this.field, true);
    }

    public void updateField(String field, boolean isWholeField) {
        if (!isWholeField){
            List<Integer> differentPieces = new ArrayList<>();
            char[] charField = this.field.toCharArray();
            char[] charField1 = field.toCharArray();
            for (int i = 0; i < fieldSize*fieldSize; i++) {
                if (charField[i] != charField1[i]) differentPieces.add(i);
            }
            this.field = field;
            int xCenter, yCenter;
            for (Integer piece : differentPieces) {
                xCenter = squareXCenter(piece / fieldSize);
                yCenter = squareYCenter(piece % fieldSize);
                drawPiece(field.charAt(piece), xCenter, yCenter);
            }
        }
        else {
            this.field = field;
            int xCenter, yCenter;
            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    xCenter = squareXCenter(j);
                    yCenter = squareYCenter(i);
                    drawPiece(field.charAt(i + j * fieldSize), xCenter, yCenter);
                }
            }
        }
    }

    private int squareXCenter(int j) {
        return (j * squareWidth()) + (squareWidth() / 2);
    }

    private int squareYCenter(int i) {
        return (i * squareHeight()) + (squareHeight() / 2);
    }

    private void drawSquare(Graphics g, int i, int j) {
        int x = j * squareWidth();
        int y = i * squareHeight();
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    private void drawPiece(char piece, int xCenter, int yCenter) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setStroke(new BasicStroke(LINE_WIDTH));
        int xFrom = xCenter - (int) (PIECE_FRACTION * (squareWidth() / 2));
        int xTo = xCenter + (int) (PIECE_FRACTION * (squareWidth() / 2));
        int yFrom = yCenter - (int) (PIECE_FRACTION * (squareHeight() / 2));
        int yTo = yCenter + (int) (PIECE_FRACTION * (squareHeight() / 2));
        int width = (int) (PIECE_FRACTION * squareWidth());
        int height = (int) (PIECE_FRACTION * squareHeight());
        g.setColor(getBackground());
        g.fillRect(xFrom - 5, yFrom - 5, width + 10, height + 10);
        switch (piece) {
            case 'X' -> {
                g.setColor(Color.BLUE);
                g.draw(new Line2D.Double(xFrom, yFrom, xTo, yTo));
                g.drawLine(xFrom, yTo, xTo, yFrom);
            }
            case 'O' -> {
                g.setColor(Color.GREEN);
                g.drawOval(xFrom, yFrom, width, height);
            }
            case 'S' -> {
                g.setColor(Color.ORANGE);
                g.drawLine(xFrom, yFrom, xFrom, yTo);
                g.drawLine(xFrom + 1, yFrom, xTo, yFrom);
                g.drawLine(xTo, yFrom + 1, xTo, yTo);
                g.drawLine(xTo - 1, yTo, xFrom + 1, yTo);
            }
            case 'T' -> {
                g.setColor(Color.MAGENTA);
                g.drawLine(xFrom, yFrom, xFrom + width / 2, yTo);
                g.drawLine(xTo, yFrom, xFrom + width / 2, yTo);
                g.drawLine(xFrom, yFrom, xTo, yFrom);
            }
            case 'D' -> {
                g.setColor(Color.CYAN);
                g.drawLine(xFrom + width / 2, yFrom, xFrom, yFrom + width / 2);
                g.drawLine(xFrom, yFrom + width / 2, xFrom + width / 2, yTo);
                g.drawLine(xFrom + width / 2, yTo, xTo, yFrom + width / 2);
                g.drawLine(xTo, yFrom + width / 2, xFrom + width / 2, yFrom);
            }
        }
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void pieceHighlight(int i, int j) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setColor(Color.RED);
        drawSquare(g, j, i);
    }

    public void pieceUnhighlight(int i, int j) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setColor(Color.BLACK);
        drawSquare(g, j, i);
    }

    interface ClickListener {
        void onSquareClick(int squareNum);
    }
}
