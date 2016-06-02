package de.mc.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.mc.game.Assets;
import de.mc.game.Constants;
import de.mc.game.CustomTextButton;

public class PauseOverlay {

    private GameScreen gameScreen;
    private Table table;
    private final Button btnClose;
    private OptionsOverlay optionsOverlay;

    public PauseOverlay(GameScreen gs) {
        gameScreen = gs;

        Image background = new Image(Assets.menuTitleButtonTexture);
        background.setWidth(background.getWidth() * Constants.SCALING);
        background.setHeight(background.getHeight() * Constants.SCALING);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);
        final Label labelTitle = new Label(Constants.LANGUAGE_STRINGS.get("Pause"), labelStyle);
        labelTitle.setAlignment(Align.center);

        final CustomTextButton btnHighScore = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("Highscore"), Assets.blueButtonBackgroundStyle);
        btnHighScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setHighscoreOverlay( new HighscoreOverlay(gameScreen));
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

        //Button in pause overlay for the return to the main menu
        final CustomTextButton btnMainMenu = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("mainmenu"), Assets.blueButtonBackgroundStyle);
        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Changes from game to main menu
                gameScreen.changeToMainManu();
                dispose();
            }
        });

        table = new Table();
        table.setBackground(background.getDrawable());
        table.setWidth(background.getWidth() * 0.95f);
        table.setHeight(background.getHeight());
        table.setPosition(Constants.WIDTH / 2 - table.getWidth() / 2, Constants.HEIGHT / 2 - table.getHeight() / 2);
        table.defaults()
                .minWidth(Value.percentWidth(1.2f))
                .prefWidth(table.getWidth() * 0.5f)
                .minHeight(Value.minHeight)
                .prefHeight(Value.percentHeight(1.2f));
        table.add(labelTitle)
                .padBottom(50);
        table.row();
        table.add(btnHighScore);
        table.row();
        table.add(btnOptions);
        table.row();
        table.add(btnMainMenu).padBottom(50);

        btnClose = new Button(Assets.menuCloseButtonStyle);
        btnClose.setWidth(btnClose.getWidth());
        btnClose.setHeight(btnClose.getHeight());
        btnClose.setPosition(table.getX() + table.getWidth() - btnClose.getWidth(), table.getY());
        final PauseOverlay pauseOverlay = this;
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setReady();
                pauseOverlay.dispose();
            }
        });

        gameScreen.stage.addActor(table);
        gameScreen.stage.addActor(btnClose);
    }

    public void dispose() {
        table.remove();
        btnClose.remove();
        if(optionsOverlay != null)
            optionsOverlay.dispose();
    }
}
