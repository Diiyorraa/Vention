package com.example.demo4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
    private String user = "postgres";
    private String password = "muzafuza";
    private BackupManager backupManager = new BackupManager();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String apiUrl = "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_250_BEST_FILMS";
        JSONArray filmsArray = new JSONArray();

        try {
            // Attempt to fetch data from the internet
            filmsArray = fetchDataFromApi(apiUrl);

            // Save the fetched data to the backup file
            backupManager.saveBackup(filmsArray);
        } catch (Exception e) {
            // Handle errors if the internet connection is absent
            e.printStackTrace();

            // Try to load data from the backup file
            filmsArray = backupManager.loadBackup(); // using here filmsArray as needed for database operations
        }
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
                String sql = "INSERT INTO top_loader_250.movies (filmId, nameRu, nameEn, year, filmLength, rating, ratingVoteCount) VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement statement = conn.prepareStatement(sql);

                PreparedStatement genreStatement = conn.prepareStatement("INSERT INTO top_loader_250.genres (genre) VALUES (?) ON CONFLICT (genre) DO NOTHING");
                PreparedStatement countryStatement = conn.prepareStatement("INSERT INTO top_loader_250.countries (country) VALUES (?) ON CONFLICT (country) DO NOTHING");

                PreparedStatement movieGenreStatement = conn.prepareStatement("INSERT INTO top_loader_250.movie_genres (movie_id, genre_id) SELECT ?, id FROM top_loader_250.genres WHERE genre = ?");
                PreparedStatement movieCountryStatement = conn.prepareStatement("INSERT INTO top_loader_250.movie_countries (movie_id, country_id) SELECT ?, id FROM top_loader_250.countries WHERE country = ?");

                for (int i = 0; i < filmsArray.length(); i++) {
                    JSONObject filmObj = filmsArray.getJSONObject(i);

                    statement.setInt(1, filmObj.getInt("filmId"));
                    statement.setString(2, filmObj.getString("nameRu"));

                    if (filmObj.isNull("nameEn")) {
                        statement.setNull(3, java.sql.Types.VARCHAR);
                    } else {
                        statement.setString(3, filmObj.getString("nameEn"));
                    }

                    statement.setInt(4, filmObj.getInt("year"));
                    statement.setString(5, filmObj.getString("filmLength"));
                    statement.setDouble(6, filmObj.getDouble("rating"));
                    statement.setInt(7, filmObj.getInt("ratingVoteCount"));

                    statement.executeUpdate();

                    JSONArray genresArray = filmObj.getJSONArray("genres");
                    JSONArray countriesArray = filmObj.getJSONArray("countries");

                    for (int j = 0; j < genresArray.length(); j++) {
                        String genre = genresArray.getJSONObject(j).getString("genre");
                        genreStatement.setString(1, genre);
                        genreStatement.executeUpdate();

                        movieGenreStatement.setInt(1, filmObj.getInt("filmId"));
                        movieGenreStatement.setString(2, genre);
                        movieGenreStatement.executeUpdate();
                    }

                    for (int k = 0; k < countriesArray.length(); k++) {
                        String country = countriesArray.getJSONObject(k).getString("country");
                        countryStatement.setString(1, country);
                        countryStatement.executeUpdate();

                        movieCountryStatement.setInt(1, filmObj.getInt("filmId"));
                        movieCountryStatement.setString(2, country);
                        movieCountryStatement.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONArray fetchDataFromApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-API-KEY", "4d8a8f60-330e-486e-8da8-f7f77c285a43");
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        return new JSONArray(content.toString());
    }
}

class BackupManager {
    private String backupFilePath = "/Users/diyorakhon/IdeaProjects/demo4/src/main/java/com/example/demo4/ExtraResoures/Movies.json";

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
