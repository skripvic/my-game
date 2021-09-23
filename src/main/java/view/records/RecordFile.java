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


public class RecordFile {
    private static final String FILE_NAME = "RecordsInformation/Records.txt";
    private List<Record> allRecords;
    private boolean isFileFound = true;

    private void readFile(){
        allRecords = new ArrayList<>();
        List<String> recordList = new ArrayList<>();
        try {
            recordList = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (NoSuchFileException e) {
            System.err.println("File was not found");
            createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        int score;
        String name;
        if (!recordList.isEmpty()) {
            for (String record : recordList) {
                Scanner scanner = new Scanner(record);
                scanner.useDelimiter(",");
                try {
                    if (scanner.hasNext())
                        name = scanner.next();
                    else throw new IllegalArgumentException();

                    if (scanner.hasNext())
                        score = Integer.parseInt(scanner.next());
                    else throw new IllegalArgumentException();

                    if (scanner.hasNext()) throw new IllegalArgumentException();
                }
                catch (IllegalArgumentException e){
                    System.err.println("Illegal format of record");
                    createNewFile();
                    readFile();
                    return;
                }
                allRecords.add(new Record(name, score));
                scanner.close();
            }
        }
    }

    private void createNewFile() {
        try {
            Files.createFile(Paths.get(FILE_NAME));
        } catch (IOException e) {
            System.err.println("File creation failed");
            isFileFound = false;
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
        if (add(newName, newScore) && isFileFound)
            try {
                Files.write(Paths.get(FILE_NAME), allRecords.stream().map(Record::toString).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void deleteAllRecords() {
        allRecords = new ArrayList<>();
        if (isFileFound)
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
