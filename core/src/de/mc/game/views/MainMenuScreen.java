package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomTextButton;

public class MainMenuScreen extends CustomScreenAdapter {

    private OptionsOverlay optionsOverlay;

    public MainMenuScreen() {
        super();

        final Image backgroundImage = new Image(Assets.backgroundStartscreen);

        final CustomTextButton playNormalBtn = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("play"), Assets.defaultTextButtonStyle);
        playNormalBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mcGame.setScreen(mcGame.gameScreen);
            }
        });

        final MainMenuScreen context = this;
        final CustomTextButton optionsBtn = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("options"), Assets.defaultTextButtonStyle);
        optionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsOverlay = new OptionsOverlay(context);
            }
        });

        final CustomTextButton playTutorialBtn = new CustomTextButton(Constants.LANGUAGE_STRINGS.get("tutorial"), Assets.defaultTextButtonStyle);
        playTutorialBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mcGame.setScreen(mcGame.tutorialScreen);
            }
        });

        Table table = new Table();
        table.setBackground(backgroundImage.getDrawable());
        table.setFillParent(true);
        table.defaults()
                .pad(30)
                .minWidth(Value.minWidth)
                .minHeight(Value.minHeight);
        table.row();
        table.add(playNormalBtn).padRight(80);
        table.add(playTutorialBtn);
        table.row();
        table.add(optionsBtn)
                .colspan(2)
                .padBottom(650);

        table.setPosition(table.getX(), Constants.HEIGHT);
        table.addAction(Actions.moveBy(0, -Constants.HEIGHT));

        stage.addActor(table);
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));
    }

    @Override
    public void show() {
        super.show();

        if(optionsOverlay != null) {
            optionsOverlay.dispose();
        }
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
