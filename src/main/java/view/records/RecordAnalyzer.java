package view.records;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class RecordAnalyzer {
    private static final String FILE_NAME = "RecordsInformation/Records.txt";
    private List<Record> allRecords;
    //private boolean isFileFound = true;


    private void readFile(){
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

        //I don't understand difference between 1. and 4.
        //In 5. what "close file" mean? There is no method "close" for file
        try {
            recordList = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (NoSuchFileException e) {
            System.err.println("File was not found");
            try {
                Files.createFile(Paths.get(FILE_NAME));

            } catch (IOException e1) {
                System.err.println("File creation failed");
            }
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

    public List<Record> getAllRecords() {
        if (allRecords == null) readFile();
        return allRecords;
    }

    private int findPosition(int newScore) {
        int size = allRecords.size();
        for (int i = 0; i < size; i++) {
            if (allRecords.get(i).getScore() < newScore){
                return i;
            }
        }
        if (size < 10) return size-1;
        return -1;
    }

    private boolean add(String newName, int newScore) {
        if (allRecords.isEmpty()) {
            allRecords.add(new Record(newName, newScore));
            return true;
        }
        int pos = findPosition(newScore);
        if (pos == -1) return false;
        allRecords.add(pos, new Record(newName, newScore));
        if (allRecords.size() > 10){
            int lastIndex = allRecords.size()-1;
            allRecords.remove(lastIndex);
        }
        return true;
    }

    public void saveNewRecord(String newName, int newScore) {
        if (add(newName, newScore))
            try {
                Files.write(Paths.get(FILE_NAME), allRecords.stream().map(Record::toString).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void deleteAllRecords() {
        allRecords = new ArrayList<>();
        try {
            Files.write(Paths.get(FILE_NAME), Collections.singleton(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewRecord(int newScore) {
        if (allRecords == null) readFile();
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

}
