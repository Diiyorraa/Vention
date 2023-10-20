package com.example.demo4.BackUpManagerForDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class BackupManager {
    private String backupFilePath = "/Users/diyorakhon/IdeaProjects/demo4/src/main/java/com/example/demo4/ExtraResoures/Movies.json\n";

    public void saveBackup(JSONArray filmsArray) {
        try (FileWriter fileWriter = new FileWriter(backupFilePath)) {
            JSONObject backupData = new JSONObject();
            backupData.put("films", filmsArray);
            fileWriter.write(backupData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray loadBackup() {
        try (FileReader fileReader = new FileReader(backupFilePath)) {
            StringBuilder content = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1) {
                content.append((char) data);
            }
            JSONObject backupData = new JSONObject(content.toString());
            return backupData.getJSONArray("films");
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray(); // Return an empty array if the backup file is not available.
        }
    }
}
