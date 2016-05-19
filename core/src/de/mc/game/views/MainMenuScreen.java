package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mc.game.Constants;
import de.mc.game.McGame;

public class MainMenuScreen extends CustomScreenAdapter {

    public MainMenuScreen(final McGame g) {
        super(g);

        Label.LabelStyle labelStyleLarge = new Label.LabelStyle(Constants.DROID_SANS_LARGE, Color.WHITE);
        Label.LabelStyle labelStyleSmall = new Label.LabelStyle(Constants.DROID_SANS_SMALL, Color.WHITE);

        final Label labelAppName = new Label(Constants.LANGUAGE_STRINGS.get("appName"), labelStyleLarge);
        labelAppName.setPosition(Constants.WIDTH / 2 - labelAppName.getWidth() / 2, Constants.HEIGHT + 100 - labelAppName.getHeight() / 2);
        labelAppName.addAction(Actions.moveBy(0, -Constants.HEIGHT / 2, 1.5f));

        final Label labelTapToStart = new Label(Constants.LANGUAGE_STRINGS.get("tapToStart"), labelStyleSmall);
        labelTapToStart.setPosition(Constants.WIDTH / 2 - labelTapToStart.getWidth() / 2, -100 - labelTapToStart.getHeight() / 2);
        labelTapToStart.addAction(Actions.moveBy(0, Constants.HEIGHT / 2, 1.5f));

        stage.addActor(labelAppName);
        stage.addActor((labelTapToStart));
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));
        stage.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons){
                mcGame.setScreen(mcGame.gameScreen);
                return true;
            }
        });
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
