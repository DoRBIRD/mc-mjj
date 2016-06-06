package de.mc.game.models.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.mc.game.McGame;
import de.mc.game.utils.Constants;

/**
 * Created by Jonas on 01/06/2016.
 */
public class DatabaseConnection {
    //Needed information for the database access
    private String url = "jdbc:mysql://dorbird.de:3306/mc-game", user = "mcgame", password = "mcgame";
    //Connection object
    private Connection connection = null;

    public DatabaseConnection() {
        try {
            //connecting to database
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established");
        } catch (Exception e) {
            McGame.AOI.toast(Constants.LANGUAGE_STRINGS.get("connectionerror"));
        }
    }

    /**
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * if the connection is active (not null) it will be closed
     */
    public void close() {
        if (connection != null) {
            try {
                this.connection.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.out.println("Logout not successfull");
            }
        }
    }
}
