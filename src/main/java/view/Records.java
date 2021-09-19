package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Records {
    private static final String FILE_NAME = "RecordsInformation/Records.txt";
    private final List<Record> allRecords;


    public Records(){
        allRecords = new ArrayList<>();
        List<String> recordList = new ArrayList<>();
        try {
            recordList = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (NoSuchFileException e) {
            System.out.println("File was not found");
            try {
                Files.write(Paths.get(FILE_NAME), Collections.singleton(""));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        int score;
        String name;
        if (!recordList.isEmpty()) {
            for (String record : recordList) {
                Scanner scanner = new Scanner(record);
                scanner.useDelimiter(",");
                name = scanner.next();
                score = Integer.parseInt(scanner.next());
                allRecords.add(new Record(name, score));
                scanner.close();
            }
        }
    }


    public String getAllScores(){
        if (allRecords.isEmpty())
            return "";
        else {
            StringBuilder result = new StringBuilder();
            for (Record record: allRecords) {
                result.append(record.getScore()).append("\n\n");
            }
            return result.toString();
        }
    }

    public String getAllNames() {
        if (allRecords.isEmpty())
            return "";
        else {
            StringBuilder result = new StringBuilder();
            for (Record record: allRecords) {
                result.append(record.getName()).append("\n\n");
            }
            return result.toString();
        }
    }

    public void saveNewRecord(String newName, int newScore) {
        for (int i = 0; i < allRecords.size(); i++) {
            if(allRecords.get(i).getScore() < newScore){
                allRecords.add(i, new Record(newName, newScore));
                break;
            }
        }
        if (allRecords.isEmpty() || allRecords.get(allRecords.size()-1).getScore() >= newScore){
            allRecords.add(new Record(newName, newScore));
        }
        if (allRecords.size() > 10){
            int lastIndex = allRecords.size()-1;
            allRecords.remove(lastIndex);
        }
        try {
            Files.write(Paths.get(FILE_NAME), recordsToStringArray());
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

    public boolean isNewRecord(int newScore) {
        if (allRecords.size() < 10){
            return true;
        }
        for (Record record : allRecords) {
            if (record.getScore() < newScore) {
                return true;
            }
        }
        return false;
    }

    private static class Record {
        String name;
        int score;

        public Record(String name, int score){
            this.name = name;
            this.score = score;
        }

        public String toString() {
            return name + "," + score;
        }

        public int getScore() {
            return score;
        }

        public String getName() {
            return name;
        }
    }

    public List<String> recordsToStringArray(){
        List<String> result = new ArrayList<>();
        for (Record record : allRecords) {
            result.add(record.toString());
        }
        return result;
    }

}
