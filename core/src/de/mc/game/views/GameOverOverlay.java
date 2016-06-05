package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.text.DecimalFormat;

import de.mc.game.models.Database.HighscoreDAO;
import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomTextButton;

public class GameOverOverlay {

    private GameScreen gameScreen;
    private Table table;
    private OptionsOverlay optionsOverlay;
    private HighscoreOverlay highscoreOverlay;

    public GameOverOverlay(GameScreen gs, float traveledDistance, int coins) {
        gameScreen = gs;

        Image background = new Image(Assets.menuTitleLargeTexture);
        background.setWidth(background.getWidth() * Constants.SCALING);
        background.setHeight(background.getHeight() * Constants.SCALING);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);
        final Label labelTitle = new Label(Constants.LANGUAGE_STRINGS.get("GAME_OVER"), labelStyle);
        labelTitle.setAlignment(Align.center);

        labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.BLACK);
        DecimalFormat df = new DecimalFormat("#.#");
        final Label labelMeter = new Label(df.format(traveledDistance) + " " + Constants.LANGUAGE_STRINGS.get("meter"), labelStyle);
        labelMeter.setAlignment(Align.center);
        labelMeter.setFontScale(1.2f);

        final Label labelStarScore = new Label(Integer.toString(coins) + " Coins", labelStyle);
        labelStarScore.setAlignment(Align.center);
        labelStarScore.setFontScale(1.2f);

        final Label labelTotalScore = new Label(Constants.LANGUAGE_STRINGS.get("total") + ": " + df.format(this.calculateHighscore(traveledDistance, coins)), labelStyle);
        labelTotalScore.setAlignment(Align.center);
        labelTotalScore.setFontScale(1.3f);
        // Get preferences
        Preferences prefs = Gdx.app.getPreferences("gamePrefs");
        // Save score into database
        new HighscoreDAO().InsertScore(prefs.getString("username", "Unknown user"), this.calculateHighscore(traveledDistance, coins));

        final CustomTextButton btnHighScore = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("highscores"), Assets.blueButtonBackgroundStyle);
        btnHighScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                highscoreOverlay = new HighscoreOverlay(gameScreen);
                dispose();
            }
        });

        final CustomTextButton btnOptions = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("options"), Assets.blueButtonBackgroundStyle);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsOverlay = new OptionsOverlay(gameScreen);
            }
        });

        final CustomTextButton btnPlayAgain = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("play_again"), Assets.blueButtonBackgroundStyle);
        btnPlayAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setReady();
                dispose();
            }
        });

        table = new Table();
        table.setBackground(background.getDrawable());
        table.setWidth(background.getWidth() * 0.95f);
        table.setHeight(background.getHeight());
        table.setPosition(Constants.WIDTH / 2 - table.getWidth() / 2, Constants.HEIGHT / 2 - table.getHeight() / 2);
        table.defaults()
                .padTop(20)
                .padBottom(20)
                .minWidth(Value.percentWidth(1.2f))
                .prefWidth(table.getWidth() * 0.65f)
                .minHeight(Value.minHeight)
                .prefHeight(Value.percentHeight(1.2f));
        table.add(labelTitle)
                .padBottom(100)
                .colspan(2);
        table.row();
        table.add(labelMeter).padBottom(10).width(table.getWidth() / 2);
        table.add(labelStarScore).padBottom(10).width(table.getWidth() / 2);
        table.row();
        table.add(labelTotalScore).colspan(2).padBottom(30);
        table.row();
        table.add(btnHighScore).colspan(2);
        table.row();
        table.add(btnOptions).colspan(2);
        table.row();
        table.add(btnPlayAgain).colspan(2).padBottom(170);

        gameScreen.stage.addActor(table);
    }

    private float calculateHighscore(float traveledDistance, int coins) {
        return traveledDistance + coins * 10;
    }

    public void dispose() {
        table.remove();
        if(optionsOverlay != null)
            optionsOverlay.dispose();
        if(highscoreOverlay != null)
            highscoreOverlay.dispose();
    }
}
