package view;

import presenter.GamePresenter;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements GameView, GameField.ClickListener{

    private static final String NAME = "MATCH THREE GAME";
    private static final String MENU = "Menu";
    private static final String NEW_GAME = "New game";
    private static final String ABOUT = "About";
    private static final String HIGH_SCORES = "High scores";
    private static final String DELETE_RECORDS = "Delete all records";
    private static final String EXIT = "Exit";

    private static final String POINT_COUNT = "                        You have %s points";
    private static final String TIME_LIMIT = "         You have %s seconds";
    private static final String ABOUT_TEXT = """
            This is a simple match three game.\s
            Just move two adjacent pieces to form groups of 3 or more of the same elements\s
            For every group you get points\s
            Try to get as much points as you can until the time runs out.""";

    private static final String START_BUTTON = "START GAME";
    private static final String NEW_RECORD_FRAME = "NEW RECORD";
    private static final String RECORDS_FRAME = "RECORDS";
    private static final String NO_RECORDS_MESSAGE = "No records yet!";
    private static final String NEW_RECORD_MESSAGE = "  You have a new record! Type your name here:  ";
    private static final String END_GAME_MESSAGE = "Game is over! Start new game?";
    private static final String[] END_GAME_OPTIONS = {"New game", "Exit"};
    private static final String END_GAME = "End game";

    private final Records records;
    private final GameField gameField;
    private GamePresenter presenter;
    Timer timer;
    JTextArea scoreText;
    JTextArea timerText;

    public GameFrame(int fieldSize) {
        super(NAME);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(600, 650));
        this.setResizable(false);

        records = new Records();

        Button refillButton = new Button("Refill field");
        refillButton.addActionListener(e -> presenter.refill());

        scoreText = new JTextArea(String.format(POINT_COUNT, 0));
        scoreText.setBackground(this.getBackground());
        scoreText.setEditable(false);

        timerText = new JTextArea(String.format(TIME_LIMIT, 0));
        timerText.setBackground(this.getBackground());
        timerText.setEditable(false);

        gameField = new GameField(fieldSize);
        gameField.setClickListener(this);

        fillGridForGameFrame(refillButton);
        setupMenu();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void fillGridForGameFrame(Button refillButton){
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0,  25, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(refillButton, constraints);

        constraints.gridx = 1;
        this.add(scoreText, constraints);

        constraints.gridx = 2;
        this.add(timerText, constraints);

        constraints.insets = new Insets(1,  3, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.ipady = 550;
        this.getContentPane().add(gameField, constraints);
    }


    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(MENU);

        JMenuItem newGameItem = new JMenuItem(NEW_GAME);
        JMenuItem highScoresItem = new JMenuItem(HIGH_SCORES);
        JMenuItem aboutItem = new JMenuItem(ABOUT);
        JMenuItem recordsDelete = new JMenuItem(DELETE_RECORDS);
        JMenuItem exitItem = new JMenuItem(EXIT);

        newGameItem.addActionListener(event -> presenter.newGame());
        aboutItem.addActionListener(event -> aboutPanel());
        highScoresItem.addActionListener(event -> recordsPanel(false));
        recordsDelete.addActionListener(event -> {
            records.deleteAllRecords();
            recordsPanel(false);
        });
        exitItem.addActionListener(event -> System.exit(0));

        menu.add(newGameItem);
        menu.add(highScoresItem);
        menu.add(aboutItem);
        menu.add(recordsDelete);
        menu.add(exitItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
    }


    private void aboutPanel(){
        JFrame frame = new JFrame(ABOUT);
        JTextArea aboutText = new JTextArea(ABOUT_TEXT);
        aboutText.setEditable(false);
        frame.setPreferredSize(new Dimension(450, 120));
        frame.add(aboutText);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }


    private void recordsPanel(boolean isEnd){
        JFrame frame = new JFrame(RECORDS_FRAME);
        frame.setLayout(new GridBagLayout());

        String names = records.getAllNames();
        String scores = records.getAllScores();
        if (!scores.equals("")) {
            scores = scores.replaceAll(",", "\n\n")
                    .replace("[", "")
                    .replace("]", "")
                    .replaceAll(" ", "");

            names = names.replaceAll(",", "\n\n")
                    .replace("[", "")
                    .replace("]", "")
                    .replaceAll(" ", "");
        }
        else{
            names = NO_RECORDS_MESSAGE;
            scores = NO_RECORDS_MESSAGE;
        }

        JTextArea textNames = new JTextArea(names);
        JTextArea textScores = new JTextArea(scores);

        textNames.setPreferredSize(new Dimension(150, 300));
        textNames.setEditable(false);
        textNames.setBackground(getBackground());
        textScores.setPreferredSize(new Dimension(150, 300));
        textScores.setEditable(false);
        textScores.setBackground(getBackground());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 110;

        frame.add(textNames, constraints);
        constraints.gridx = 1;
        constraints.ipadx = 25;
        frame.add(textScores, constraints);

        if (isEnd) {
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    endGameFrame();
                }
            });
        }
        frame.setPreferredSize(new Dimension(400, 450));
        frame.setLocation(400, 150);
        frame.pack();
        frame.setVisible(true);
    }

    public void createStartPanel() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(150, 150));
        JButton startButton = new JButton(START_BUTTON);
        frame.add(startButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        startButton.addActionListener(e -> {
            frame.setVisible(false);
            presenter.newGame();
        });
    }


    public void changeTime(int time){
        timerText.setText(String.format(TIME_LIMIT, time));
    }

    @Override
    public void updateScore(int score){
        scoreText.setText(String.format(POINT_COUNT, score));
    }


    private void newRecordPanel(){
        JFrame frame = new JFrame(NEW_RECORD_FRAME);
        frame.setLayout(new GridBagLayout());

        JTextField newRecordField = new JTextField(15);
        JTextArea newRecordText = new JTextArea(NEW_RECORD_MESSAGE);
        newRecordText.setEditable(false);
        newRecordText.setBackground(frame.getBackground());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(0,  0, 5, 0);
        constraints.fill = GridBagConstraints.BOTH;
        frame.add(newRecordText, constraints);

        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        frame.add(newRecordField, constraints);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        newRecordField.addActionListener(e -> {
            frame.dispose();
            String name = newRecordField.getText();
            records.saveNewRecord(name);
            recordsPanel(true);
        });
    }


    @Override
    public void start(String field) {
        // CR: seems redundnant, please check
        gameField.updateField(field);
        if (timer != null) timer.stop();
        timer = new Timer(presenter, this);
    }

    @Override
    public void update(String field) {
        gameField.updateField(field);
    }

    @Override
    public void attachPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void end(int score) {
        if (!records.isNewRecord(score))
            endGameFrame();
        else newRecordPanel();
    }

    private void endGameFrame(){
        int choice = JOptionPane.showOptionDialog(this, END_GAME_MESSAGE, END_GAME,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, END_GAME_OPTIONS, END_GAME_OPTIONS[1]);
        if (choice == JOptionPane.YES_OPTION) {
            presenter.newGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onSquareClick(int squareNum) {
        presenter.movePieces(squareNum);
    }

}
