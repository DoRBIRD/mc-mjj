package de.mc.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CustomProgressBar extends ProgressBar {

    private Image backgroundLeft, backgroundRight, knobLeft;
    private Stage stage;

    public CustomProgressBar(Stage stage, float min, float max, float stepSize, boolean vertical, ProgressBarStyle style) {
        super(min, max, stepSize, vertical, style);

        this.stage = stage;

        Skin skin = new Skin(new TextureAtlas("progressbar/default-progress-bar.pack"));
        backgroundLeft = new Image(skin.getDrawable("progress_bar_background_left"));
        backgroundRight = new Image(skin.getDrawable("progress_bar_background_right"));
        knobLeft = new Image(skin.getDrawable("progress_bar_knob_left_red"));

        setPosition(getX(), getY());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        backgroundLeft.setPosition(x - backgroundLeft.getWidth(), y);
        backgroundRight.setPosition(x + getWidth(), y);
        knobLeft.setPosition(x - knobLeft.getWidth(), y);

        stage.addActor(backgroundLeft);
        stage.addActor(backgroundRight);
        stage.addActor(knobLeft);
    }

    public void setKnobLeftImage(Drawable knobLeft) {
        this.knobLeft = new Image(knobLeft);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible) {
            stage.addActor(backgroundLeft);
            stage.addActor(backgroundRight);
            stage.addActor(knobLeft);
        } else {
            backgroundLeft.remove();
            backgroundRight.remove();
            knobLeft.remove();
        }
    }
}
