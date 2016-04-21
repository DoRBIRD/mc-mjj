package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.mc.game.Constants;
import de.mc.game.McGame;

public class CustomScreenAdapter extends ScreenAdapter {

    protected final McGame mcGame;
    protected Stage stage;
    protected OrthographicCamera camera;

    public CustomScreenAdapter (McGame g) {
        super();

        mcGame = g;

        stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void show() {
        super.show();

        mcGame.inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        super.hide();

        mcGame.inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void dispose() {
        super.dispose();

        mcGame.inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }
}
