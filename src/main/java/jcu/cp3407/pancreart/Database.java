package jcu.cp3407.pancreart;

import java.sql.*;
import java.util.ArrayList;

import jcu.cp3407.pancreart.model.*;

public class Database {

    final static String NAME = "pancreart.sql";

    Connection connection;

    public static void main(String[] args) {
        Database database = new Database();
        try {
            database.create(NAME);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void create(String fileName) throws SQLException {

        // jdbc:raima:rdm://local
        String url = "jdbc:sqlite:C:/" + NAME;

        String query =
            "CREATE TABLE IF NOT EXISTS `event` (\n" +
                "`user_id` integer NOT NULL,\n" +
                "`time` integer NOT NULL,\n" +
                "`amount` real NOT NULL\n" +
                ");";

        connection = DriverManager.getConnection(url);

        if (connection != null) {
            Statement statement = connection.createStatement();
            statement.execute(query);
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("The driver name is " + metaData.getDriverName());
            System.out.println("A new database has been created.");
        }
    }

    public ArrayList<Event> select() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM `event`");
        while (resultSet.next()) {
            events.add(new Event(
                resultSet.getInt("user_id"),
                resultSet.getInt("time"),
                resultSet.getFloat("amount")
            ));
        }
        return events;
    }
}
