package de.mc.game.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CustomTextButton extends TextButton {

    public CustomTextButton(String text, TextButtonStyle style) {
        super(text, style);

        final TextButton btn = this;

        btn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.getLabel().setHeight(btn.getHeight() - 30);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btn.getLabel().setHeight(btn.getHeight());
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                btn.getLabel().setHeight(btn.getHeight());
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }
}
