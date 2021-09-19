package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Records {

    //why i didn't put records into resources:
    //resources contain files that we use but not change
    //nd i can't really make output stream from resources

    private static final String FILE_NAME = "RecordsInformation/Records.txt";
    /*
    CR:
    it's usually better not to have multiple presentations for one thing, since you have to update all them on change
    so i'd probably have only List<Record> where Record is:
    class Record { String name; int score; String toString() { return name + "," + score; }
     */
    private List<String> allRecords;
    private final List<String> nameList;
    private final List<Integer> scoreList;
    private int newScore;


    public Records(){
        scoreList = new ArrayList<>();
        nameList = new ArrayList<>();
        readFile();
    }


    private void readFile(){
        try {
            allRecords = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (IOException e) {
            // CR: we can try to recreate records file in this case
            // CR: or at least assign allRecords = new ArrayList<>() , it's up to you
            e.printStackTrace();
        }
        separateNamesAndScores();
    }

    private void separateNamesAndScores() {
        int score;
        for (String record : allRecords) {
            // CR: please use Scanner instead
            score = Integer.parseInt(record.substring(record.lastIndexOf(",") + 1));
            nameList.add(record.substring(0, record.lastIndexOf(",")));
            scoreList.add(score);
        }
    }


    public String getAllScores(){
        if (allRecords.isEmpty())
            return "";
        else
            return scoreList.toString();
    }


    public String getAllNames() {
        if (allRecords.isEmpty())
            return "";
        else
            return nameList.toString();
    }


    public void saveNewRecord(String newName) {
        for (int i = 0; i < scoreList.size(); i++) {
            if(scoreList.get(i) < newScore){
                scoreList.add(i, newScore);
                nameList.add(i, newName);
                allRecords.add(i, newName+","+newScore);
                break;
            }
        }
        if (scoreList.isEmpty() || scoreList.get(scoreList.size()-1) >= newScore){
            nameList.add(newName);
            scoreList.add(newScore);
            allRecords.add(newName + "," + newScore);
        }
        if (scoreList.size() > 10){
            int lastIndex = scoreList.size()-1;
            scoreList.remove(lastIndex);
            nameList.remove(lastIndex);
            allRecords.remove(lastIndex);
        }
        try {
            Files.write(Paths.get(FILE_NAME), allRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllRecords() {
        try {
            Files.write(Paths.get(FILE_NAME), Collections.singleton(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CR: i think it's better not to have newScore as part of a `Records` class state, you can pass it again in saveNewRecord
    public boolean isNewRecord(int newScore) {
        if (scoreList.size() < 10){
            this.newScore = newScore;
            return true;
        }
        for (Integer score : scoreList) {
            if (score < newScore) {
                this.newScore = newScore;
                return true;
            }
        }
        return false;
    }
}