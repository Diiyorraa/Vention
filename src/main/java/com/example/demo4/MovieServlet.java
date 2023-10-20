package com.example.demo4;
//Allows users to get all information about a movie and full list of movies.
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "movieServlet", value = "/movie-servlet/*")
public class MovieServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // here handling the request for the full list of movies
            getFullListOfMovies(response);
        } else {
            // Extract movie ID from the URL and retrieve specific movie information
            String movieId = pathInfo.substring(1); // Remove leading slash
            getMovieById(response, movieId);
        }
    }

    private void getFullListOfMovies(HttpServletResponse response) throws IOException {
        String apiUrl = "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_250_BEST_FILMS";

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

            JSONObject obj = new JSONObject(content.toString());
            JSONArray filmsArray = obj.getJSONArray("films");

            response.getWriter().print(filmsArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void getMovieById(HttpServletResponse response, String movieId) throws IOException {
        String apiUrl = "https://kinopoiskapiunofficial.tech/api/v2.1/films/" + movieId;

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

            JSONObject movieObj = new JSONObject(content.toString());
            response.getWriter().print(movieObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

