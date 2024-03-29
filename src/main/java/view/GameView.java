package view;

import presenter.GamePresenter;

public interface GameView {

    void start(String field);

    void update(String field, boolean isWholeField);

    void createStartPanel();

    void updateScore(int score);

    void attachPresenter(GamePresenter presenter);

    void end(int score);

}
