package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomTextButton;

public class MainMenuScreen extends CustomScreenAdapter {

    public MainMenuScreen() {
        super();

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);

        final Label labelAppName = new Label(Constants.LANGUAGE_STRINGS.get("appName"), labelStyle);
        labelAppName.setFontScale(1.5f);

        /*final Label labelTapToStart = new Label(Constants.LANGUAGE_STRINGS.get("tapToStart"), labelStyle);
        labelTapToStart.setFontScale(0.7f);
*/
        final CustomTextButton playNormal = new CustomTextButton("Play", Assets.blueButtonBackgroundStyle);
        playNormal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mcGame.setScreen(mcGame.gameScreen);
            }
        });
        final CustomTextButton playTutorial = new CustomTextButton("Tutorial", Assets.blueButtonBackgroundStyle);
        playTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mcGame.setScreen(mcGame.tutorialScreen);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.defaults()
                .pad(20)
                .minWidth(Value.minWidth)
                .minHeight(Value.minHeight);
        table.add(labelAppName).padBottom(100);
/*        table.row();
        table.add(labelTapToStart);*/
        table.row();
        table.add(playNormal);
        table.row();
        table.add(playTutorial);



        table.setPosition(table.getX(), Constants.HEIGHT);
        table.addAction(Actions.moveBy(0, -Constants.HEIGHT));

        stage.addActor(table);
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));
        /*stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                mcGame.setScreen(mcGame.gameScreen);
                //mcGame.setScreen(mcGame.tutorialScreen);
                return true;
            }
        });*/
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    public void dispose() {
        super.dispose();
    }
}
