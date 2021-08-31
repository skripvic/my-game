package view;

import presenter.GamePresenter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    private static final String NEW_RECORD_MESSAGE = "  You have a new record! Type your name, use only 3 letters:  ";
    private static final String END_GAME_MESSAGE = "Game is over! Start new game?";
    private static final String[] END_GAME_OPTIONS = {"New game", "Exit"};
    private static final String END_GAME = "End game";


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
        GridBagConstraints constraints = new GridBagConstraints();

        Button refillButton = new Button("Refill field");
        refillButton.addActionListener(e -> presenter.refill());
        scoreText = new JTextArea(String.format(POINT_COUNT, 0));
        timerText = new JTextArea(String.format(TIME_LIMIT, 0));
        scoreText.setBackground(this.getBackground());
        timerText.setBackground(this.getBackground());
        scoreText.setEditable(false);
        timerText.setEditable(false);

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
        gameField = new GameField(fieldSize);
        gameField.setClickListener(this);
        this.getContentPane().add(gameField, constraints);

        setupMenu();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
        recordsDelete.addActionListener(event -> deleteAllRecords());
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
        JFrame frame = new JFrame();
        JTextArea aboutText = new JTextArea(ABOUT_TEXT);
        aboutText.setEditable(false);
        frame.setPreferredSize(new Dimension(400, 120));
        frame.add(aboutText);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }


    private void recordsPanel(boolean isEnd){
        JFrame frame = new JFrame("Records");
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("Records"))){
            for (int i = 0; i < 8; i++) {
                content.append(reader.readLine());
                content.append("\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JTextArea text = new JTextArea(content.toString());
        text.setPreferredSize(new Dimension(300, 300));
        text.setEditable(false);

        if (isEnd) {
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    endGameFrame();
                }
            });
        }
        frame.setPreferredSize(new Dimension(400, 300));
        frame.add(text);
        frame.setLocation(400, 150);
        frame.pack();
        frame.setVisible(true);
    }

    private void deleteAllRecords(){
        try {
            List <String> lines = Files.readAllLines(Paths.get("DefaultRecords"));
            Files.write(Paths.get("Records"), lines);
            recordsPanel(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private boolean isNewRecord(int score){
        try {
            List <String> lines = Files.readAllLines(Paths.get("Records"));
            int record, indexOfNumber = 71;
            for(String s : lines) {
                if (s.contains("RECORDS")) continue;
                record = Integer.parseInt(s.substring(indexOfNumber));
                if (score > record){
                    newRecordPanel(lines, score, s);
                    System.out.println("NEW RECORD");
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void newRecordPanel(List<String> lines, int score, String string){
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
            StringBuilder name = new StringBuilder(newRecordField.getText());
            while (name.length() < 3) name.append(" ");
            String finalName = name.toString();
            if (name.length() > 3) finalName = finalName.substring(0,3);
            saveNewRecord(lines, score, finalName, string);
        });
    }

    private void saveNewRecord(List<String> lines, int score, String name, String string){
        int indexOfName = 36, indexOfNumber = 71, indexOfPlace = 16;
        string = string.replace(string.substring(indexOfName, indexOfName+3), name);
        string = string.replace(string.substring(indexOfNumber), Integer.toString(score));
        List<String> newLines = new ArrayList<>();
        boolean changePlace = false;
        for(String s : lines) {
            if (newLines.size() == 8) break;
            if (s.contains("RECORDS")) {
                newLines.add(s);
                continue;
            }
            if (s.charAt(indexOfPlace) == string.charAt(indexOfPlace)){
                changePlace = true;
                newLines.add(string);
            }
            if (changePlace) s = s.replace(s.substring(indexOfPlace, indexOfPlace+1), Integer.toString(lines.indexOf(s)+1));
            newLines.add(s);
        }
        try {
            Files.write(Paths.get("Records"), newLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordsPanel(true);
    }

    @Override
    public void start(String field) {
        gameField.updateField(field);
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
        if (!isNewRecord(score)) endGameFrame();
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
