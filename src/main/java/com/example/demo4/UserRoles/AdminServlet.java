package com.example.demo4.UserRoles;


import com.example.demo4.service.UpdateFilm;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "adminServlet", value = "/admin-servlet")
public class AdminServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("runUpdate".equals(action)) {
            runDBUpdate();
            response.getWriter().write("Database update task executed successfully.");
        } else if ("blockUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            blockUser(userId);
            response.getWriter().write("User with ID " + userId + " has been blocked.");
        } else if ("unblockUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            unblockUser(userId);
            response.getWriter().write("User with ID " + userId + " has been unblocked.");
        } else if ("blockFilm".equals(action)) {
            int filmId = Integer.parseInt(request.getParameter("filmId"));
            blockFilm(filmId);
            response.getWriter().write("Film with ID " + filmId + " has been blocked.");
        } else if ("unblockFilm".equals(action)) {
            int filmId = Integer.parseInt(request.getParameter("filmId"));
            unblockFilm(filmId);
            response.getWriter().write("Film with ID " + filmId + " has been unblocked.");
        } else {
            response.getWriter().write("Invalid action.");
        }
    }

    private void runDBUpdate() {
        // You can call the updateStaleFilms method from the UpdateFilm class or any other custom logic.
        UpdateFilm updateFilm = new UpdateFilm();
        updateFilm.updateStaleFilms();
    }


    private void blockUser(int userId) {
        String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
        String user = "postgres";
        String password = "muzafuza";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            String blockUserSQL = "UPDATE users SET blocked = true WHERE user_id = ?";
            PreparedStatement blockUserStatement = conn.prepareStatement(blockUserSQL);
            blockUserStatement.setInt(1, userId);
            int rowsUpdated = blockUserStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User with ID " + userId + " has been successfully blocked.");
            } else {
                System.out.println("User with ID " + userId + " was not found or couldn't be blocked.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void unblockUser(int userId) {
        String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
        String user = "postgres";
        String password = "muzafuza";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            String unblockUserSQL = "UPDATE users SET blocked = false WHERE user_id = ?";
            PreparedStatement unblockUserStatement = conn.prepareStatement(unblockUserSQL);
            unblockUserStatement.setInt(1, userId);
            int rowsUpdated = unblockUserStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User with ID " + userId + " has been successfully unblocked.");
            } else {
                System.out.println("User with ID " + userId + " was not found or couldn't be unblocked.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void blockFilm(int filmId) {
        String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
        String user = "postgres";
        String password = "muzafuza";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            String blockFilmSQL = "UPDATE films SET blocked = true WHERE film_id = ?";
            PreparedStatement blockFilmStatement = conn.prepareStatement(blockFilmSQL);
            blockFilmStatement.setInt(1, filmId);
            int rowsUpdated = blockFilmStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Film " + filmId + " has been successfully blocked.");
            } else {
                // Film not found or couldn't be blocked.
                System.out.println("Film " + filmId + " was not found or couldn't be blocked.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void unblockFilm(int filmId) {
        String dbUrl = "jdbc:postgresql://localhost:5043/top_loader";
        String user = "postgres";
        String password = "muzafuza";

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password)) {
            String unblockFilmSQL = "UPDATE films SET blocked = false WHERE film_id = ?";
            PreparedStatement unblockFilmStatement = conn.prepareStatement(unblockFilmSQL);
            unblockFilmStatement.setInt(1, filmId);
            int rowsUpdated = unblockFilmStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Film " + filmId + " has been successfully unblocked."); }
            else {
                System.out.println("Film " + filmId + " was not found or couldn't be unblocked.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
