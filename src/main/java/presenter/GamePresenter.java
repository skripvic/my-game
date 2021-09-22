package presenter;

import view.*;
import model.*;

public class GamePresenter implements Runnable, FieldListener {
    private GameView view;
    private final Field field;

    public GamePresenter(Field field) {
        this.field = field;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void run() {
        field.setListener(this);
        view.attachPresenter(this);
        view.createStartPanel();
    }

    public void movePieces(int pieceNum) {
        field.update(pieceNum);
    }

    public void updateScore(int score){
        view.updateScore(score);
    }

    @Override
    public void update(boolean isWholeField) {
        view.update(field.toString(), isWholeField);
    }

    public void newGame() {
        field.clear();
        field.fillField();
        view.start(field.toString());
    }

    public void refill(){
        field.fillField();
    }

    public void end(){
        view.end(field.getScore());
        field.clear();
    }

}
