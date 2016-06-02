package de.mc.game.models;

/**
 * Created by Jenni on 02.06.2016.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class HighscoreDAO {
    //DB connection
    private Connection connection = null;

    //prepared Statements for db connection
    PreparedStatement insertScore;
    PreparedStatement getScores;

    //SQL Querys
    String sqlInsertScore;
    String sqlGetScores;

    public HighscoreDAO(){
        /*
		 * set the connection and create the prepared Statements for the
		 * Database access
		 */
        connection = new DatabaseConnection().getConnection();
        createPreparedStatements();
    }

    private void createPreparedStatements(){
        //Set SQL Querys
        this.sqlGetScores = "SELECT score, user FROM Highscore ORDER BY score desc LIMIT 5;";
        this.sqlInsertScore = "INSERT INTO Highscore (score, user) values (?,?);";
        try{
           this.getScores = this.connection.prepareStatement(this.sqlGetScores);
           this.insertScore = this.connection.prepareStatement(this.sqlInsertScore);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> getScores(){
        //List for the Highscores
        List<String> scores = new LinkedList<String>();
        //User and score value
        String user, score;
        //Result set for scores
        ResultSet rs;
        try{
            //accessing Database
            rs = this.getScores.executeQuery();
            while(rs.next()){
                //get the results from the resultset
                score = "" + rs.getInt(1);
                user = rs.getString(2);
                scores.add(user + ": " + score);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        //returning top 5 scores
        return scores;
    }

    /**
     * Inserts a new score related to a player into the Database
     * @param user name of the user with the new score
     * @param score new score
     */
    public void InsertScore(String user, float score){
        try{
            this.insertScore.setString(2, user);
            this.insertScore.setFloat(1,score);
            this.insertScore.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
