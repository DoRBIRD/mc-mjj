package de.mc.game.models;

import com.badlogic.gdx.utils.Array;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jonas on 01/06/2016.
 */
public class DatabaseConnection {
    public Array<String> getHighscores() {
        Array<String> result = new Array<String>();
        try { //doesnt find driver yet :/
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://dorbird.de:3306", "mcgame", "mcgame");
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rs = st.executeQuery("select * from Highscore ORDER BY score LIMIT 5");
            while (rs.next()) {
                String user = rs.getString("user");
                String score = rs.getString("score");
                result.add(user + ": " + score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return result;
    }
}
