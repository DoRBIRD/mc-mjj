package de.mc.game.models.Database;

/**
 * Created by Jenni on 02.06.2016.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.mc.game.McGame;
import de.mc.game.utils.Constants;

public class HighscoreDAO {
    //prepared Statements for db connection
    PreparedStatement insertScore;
    PreparedStatement getScores;
    //SQL Queries
    String sqlInsertScore;
    String sqlGetScores;
    //DB connection
    private Connection connection = null;

    public HighscoreDAO() {
        /*
         * set the connection and create the prepared Statements for the
		 * Database access
		 */
        connection = new de.mc.game.models.Database.DatabaseConnection().getConnection();
        createPreparedStatements();
    }

    private void createPreparedStatements() {
        //Set SQL Queries
        sqlGetScores = "SELECT score, user FROM Highscore ORDER BY score desc LIMIT 10;";
        sqlInsertScore = "INSERT INTO Highscore (score, user) values (?,?);";
        try {
            getScores = connection.prepareStatement(sqlGetScores);
            insertScore = connection.prepareStatement(sqlInsertScore);
        } catch (SQLException e) {
            McGame.AOI.toast(Constants.LANGUAGE_STRINGS.get("connectionerror"));
        }
    }

    public List<String> getScores() {
        //List for the Highscores
        List<String> scores = new LinkedList<String>();
        //User and score value
        String user;
        float score;
        //Result set for scores
        ResultSet rs;
        try {
            //accessing Database
            rs = getScores.executeQuery();
            while(rs.next()) {
                //get the results from the resultset
                score = rs.getFloat(1);
                user = rs.getString(2);
                scores.add(user + ": " + score);
            }
        } catch (SQLException e) {
            McGame.AOI.toast(Constants.LANGUAGE_STRINGS.get("connectionerror"));
        }
        //returning top 5 scores
        return scores;
    }

    /**
     * Inserts a new score related to a player into the Database
     *
     * @param user  name of the user with the new score
     * @param score new score
     */
    public void InsertScore(String user, float score) {
        try {
            insertScore.setString(2, user);
            insertScore.setFloat(1, score);
            insertScore.executeUpdate();
        } catch (SQLException e) {
            McGame.AOI.toast(Constants.LANGUAGE_STRINGS.get("connectionerror"));
        }
    }
}
