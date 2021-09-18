package view;

import presenter.GamePresenter;

import javax.swing.*;
import java.awt.event.ActionEvent;

//why my timer doesn't extend java.swing.Timer:
//i can't stop/restart timer and change gameTime from actionListener in super()

public class Timer {
    private final GamePresenter presenter;
    private final GameFrame view;
    int gameTime = 30;

    javax.swing.Timer timer = new javax.swing.Timer(1000, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameTime--;
            view.changeTime(gameTime);
            if (gameTime == 0){
                presenter.end();
                timer.stop();
            }
            else
                timer.restart();
        }
    });

    public Timer (GamePresenter presenter, GameFrame view) {
        view.changeTime(gameTime);
        this.view = view;
        this.presenter = presenter;
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}