package com.example.demo4.service;
//Update by scheduler for all films which weren’t updated for more than two days
import org.json.JSONObject;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@WebListener
public class UpdateFilm implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateStaleFilms, 0, 1, TimeUnit.DAYS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

    public void updateStaleFilms() {
        // My database connection details
        String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
        String user = "postgres";
        String password = "muzafuza";

        //  the time threshold for "stale" films (e.g., 2 days)
        long staleThresholdMillis = 2 * 24 * 60 * 60 * 1000;

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            // Preparing a query to retrieve films that need updating
            String selectStaleFilmsSQL = "SELECT filmId, lastUpdated FROM top_loader_250.movies WHERE lastUpdated IS NULL OR lastUpdated < ?";
            PreparedStatement selectStaleFilmsStatement = conn.prepareStatement(selectStaleFilmsSQL);
            Date staleThresholdDate = new Date(System.currentTimeMillis() - staleThresholdMillis);
            selectStaleFilmsStatement.setTimestamp(1, new java.sql.Timestamp(staleThresholdDate.getTime()));

            ResultSet resultSet = selectStaleFilmsStatement.executeQuery();

            // Loop through the stale films and update them
            while (resultSet.next()) {
                int filmId = resultSet.getInt("filmId");
                updateFilmDetails(filmId, conn);

                // Update the lastUpdated timestamp to prevent future updates
                String updateLastUpdatedSQL = "UPDATE top_loader_250.movies SET lastUpdated = ? WHERE filmId = ?";
                PreparedStatement updateLastUpdatedStatement = conn.prepareStatement(updateLastUpdatedSQL);
                updateLastUpdatedStatement.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                updateLastUpdatedStatement.setInt(2, filmId);
                updateLastUpdatedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateFilmDetails(int filmId, Connection conn) {
        String apiUrl = "https://kinopoiskapiunofficial.tech/api/v2.2/films/" + filmId;

        try {
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

            JSONObject filmObj = new JSONObject(content.toString());

            // Update film details in the database
            String updateFilmDetailsSQL = "UPDATE top_loader_250.movies SET nameRu = ?, nameEn = ?, year = ?, filmLength = ?, rating = ?, ratingVoteCount = ? WHERE filmId = ?";
            PreparedStatement updateFilmDetailsStatement = conn.prepareStatement(updateFilmDetailsSQL);
            updateFilmDetailsStatement.setString(1, filmObj.getString("nameRu"));

            if (filmObj.isNull("nameEn")) {
                updateFilmDetailsStatement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                updateFilmDetailsStatement.setString(2, filmObj.getString("nameEn"));
            }

            updateFilmDetailsStatement.setInt(3, filmObj.getInt("year"));
            updateFilmDetailsStatement.setString(4, filmObj.getString("filmLength"));
            updateFilmDetailsStatement.setDouble(5, filmObj.getDouble("rating"));
            updateFilmDetailsStatement.setInt(6, filmObj.getInt("ratingVoteCount"));
            updateFilmDetailsStatement.setInt(7, filmId);
            updateFilmDetailsStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
