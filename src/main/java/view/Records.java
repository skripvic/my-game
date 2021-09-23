package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Records {
    private static final String FILE_NAME = "RecordsInformation/Records.txt";
    private final List<Record> allRecords;


    public Records(){
        allRecords = new ArrayList<>();
        List<String> recordList = new ArrayList<>();
        /*
        CR:
        1. try to read from file
        2. if failed - try to create file (use Files.createFile())
        3. if 2. has failed - print to System.err and continue with only in memory state. return
        4. scan records from file. if scanning failed - recreate file as empty file (same as steps 2 and 3)
        5. close file if step 3. wasn't executed
        also invoke this logic on the first call of getAllScores instead of doing this in constructor
         */
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

    // CR: make Record public outer class and return List<Record> instead of getAllScores and getAllNames
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
        /*
        CR:
        private int insertPos(int newScore) { ... }

        private boolean add(String name, int newScore) {
          if (allRecords.isEmpty()) {
            allRecords.add(..);
            return true;
          }
          int pos = insertPos(newScore);
          // we have 10 records and they are all cooler then a new one
          if (pos == -1) return false;
          allRecords.set(pos, ...);
          return true;
        }


        public void saveNewRecord(String newName, int newScore) {
          if (add(newName, newScore) Files.write(....);
        }
         */
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
            Files.write(Paths.get(FILE_NAME), allRecords.stream().map(Record::toString).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllRecords() {
        // CR: this method should also update allRecords , no?
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
        // CR: private final
        String name;
        // CR: private final
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
}
