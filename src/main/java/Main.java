import model.Field;
import presenter.GamePresenter;
import view.*;

public class Main {

    private static final int FIELD_SIZE = 10;

    public static void main(String[] args) {
        GameView view = new GameFrame(FIELD_SIZE);
        Field field = new Field(FIELD_SIZE);
        GamePresenter presenter = new GamePresenter(field);
        presenter.setView(view);
        presenter.run();
    }
}