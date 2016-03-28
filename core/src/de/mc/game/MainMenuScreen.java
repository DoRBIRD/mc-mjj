package de.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {

    final McGame mcGame;

    OrthographicCamera camera;

    public MainMenuScreen(final McGame g) {
        mcGame = g;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mcGame.width, mcGame.height);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mcGame.batch.setProjectionMatrix(camera.combined);

        mcGame.batch.begin();
        mcGame.glyphLayout.setText(mcGame.droidSansLarge, mcGame.languageStrings.get("appName"));
        mcGame.droidSansLarge.draw(mcGame.batch, mcGame.glyphLayout, mcGame.width / 2 - mcGame.glyphLayout.width / 2, 500 - mcGame.glyphLayout.height / 2);
        mcGame.glyphLayout.setText(mcGame.droidSansSmall, mcGame.languageStrings.get("tapToStart"));
        mcGame.droidSansSmall.draw(mcGame.batch, mcGame.glyphLayout, mcGame.width / 2 - mcGame.glyphLayout.width / 2, 350 - mcGame.glyphLayout.height / 2);
        mcGame.batch.end();

        if (Gdx.input.isTouched()) {
            mcGame.switchScreen(mcGame.gameScreen);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
