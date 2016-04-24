package de.mc.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.mc.game.Constants;
import de.mc.game.McGame;

public class GameOverOverlay {

    private GameScreen gameScreen;
    private Table table;

    public GameOverOverlay(McGame mcGame, GameScreen gs, int endScore) {
        gameScreen = gs;
        
        Label.LabelStyle labelStyle = new Label.LabelStyle(mcGame.droidSansMedium, Color.BLACK);
        final Label labelTitle = new Label("Game over", labelStyle);
        labelTitle.setAlignment(Align.center);

        final Label labelScore = new Label("Your score: " + endScore, labelStyle);
        labelScore.setAlignment(Align.center);

        final TextButton btnHighScore = new TextButton("Highscores", mcGame.defaultTextButtonStyle);
        btnHighScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                
            }
        });

        final TextButton btnPlayAgain = new TextButton("Play again", mcGame.defaultTextButtonStyle);
        btnPlayAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setReady();
                dispose();
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.defaults()
                .pad(20)
                .minWidth(Value.minWidth)
                .prefWidth(Constants.WIDTH / 2)
                .minHeight(Value.minHeight)
                .prefHeight(Value.percentHeight(1.5f));
        table.add(labelTitle);
        table.row();
        table.add(labelScore)
                .padBottom(100);
        table.row();
        table.add(btnHighScore);
        table.row();
        table.add(btnPlayAgain);

        gameScreen.stage.addActor(table);
    }

    public void dispose() {
        table.remove();
    }
}