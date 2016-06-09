package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.mc.game.McGame;
import de.mc.game.utils.Constants;

public class CustomScreenAdapter extends ScreenAdapter {

    protected final McGame mcGame;
    protected Stage stage;
    protected OrthographicCamera camera;

    public CustomScreenAdapter() {
        mcGame = (McGame) Gdx.app.getApplicationListener();

        stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void show() {
        mcGame.inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        mcGame.batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        mcGame.inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void dispose() {
        mcGame.inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }


}
