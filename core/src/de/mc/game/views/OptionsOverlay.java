package de.mc.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.mc.game.McGame;
import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomTextButton;

public class OptionsOverlay {

    private Table table;
    private final Button btnClose;
    private final TextField textFieldName;
    private Preferences prefs;

    public OptionsOverlay(CustomScreenAdapter currentScreen) {
        Image background = new Image(Assets.menuTitleButtonLargeTexture);
        background.setWidth(background.getWidth() * Constants.SCALING);
        background.setHeight(background.getHeight() * Constants.SCALING);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);
        final Label labelTitle = new Label(Constants.LANGUAGE_STRINGS.get("options"), labelStyle);
        labelTitle.setAlignment(Align.center);

        prefs = Gdx.app.getPreferences(Constants.DEFAULT_PREFS);
        textFieldName = new TextField(prefs.getString(Constants.PREFS_USERNAME, Constants.LANGUAGE_STRINGS.get("yourname")), Assets.defaultTextFieldStyle);
        textFieldName.setAlignment(Align.center);

        textFieldName.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER) {
                    submitUsername();
                }
                return true;
            }
        });

        final CustomTextButton btnSubmitName = new CustomTextButton("Speichern", Assets.blueButtonBackgroundStyle);
        btnSubmitName.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submitUsername();
            }
        });

        Label.LabelStyle labelStyleWithBackground = new Label.LabelStyle(labelStyle);
        labelStyleWithBackground.background = Assets.defaultSkin.getDrawable("backgroundLabelGreen");
        final Label labelSound = new Label(Constants.LANGUAGE_STRINGS.get("sound"), labelStyleWithBackground);
        labelSound.setAlignment(Align.center);

        final Slider sliderSound = new Slider(0, 100, 10, false, Assets.defaultSliderStyle);
        sliderSound.setValue(prefs.getFloat(Constants.PREFS_SOUND_VOL));
        sliderSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putFloat(Constants.PREFS_SOUND_VOL, sliderSound.getValue());
                prefs.flush();
            }
        });

        final Slider sliderMusic = new Slider(0, 100, 10, false, Assets.defaultSliderStyle);
        sliderMusic.setValue(prefs.getFloat(Constants.PREFS_MUSIC_VOL));
        sliderMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prefs.putFloat(Constants.PREFS_MUSIC_VOL, sliderMusic.getValue());
                prefs.flush();
            }
        });

        final Label labelMusic = new Label(Constants.LANGUAGE_STRINGS.get("music"), labelStyleWithBackground);
        labelMusic.setAlignment(Align.center);

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
                .padBottom(150)
                .colspan(2);
        table.row();
        table.add(textFieldName).colspan(2).height(Value.percentHeight(1.35f));
        table.row();
        table.add(btnSubmitName).colspan(2).height(Value.percentHeight(1.35f)).padBottom(130);
        table.row();
        table.add(labelSound)
                .prefWidth(table.getWidth() * 0.35f)
                .padRight(30)
                .padBottom(50);
        table.add(sliderSound)
                .prefWidth(table.getWidth() * 0.45f)
                .padBottom(50);
        table.row();
        table.add(labelMusic).prefWidth(table.getWidth() * 0.35f).padRight(30).padBottom(340);
        table.add(sliderMusic).prefWidth(table.getWidth() * 0.45f).padBottom(340);

        btnClose = new Button(Assets.menuCloseButtonStyle);
        btnClose.setWidth(btnClose.getWidth());
        btnClose.setHeight(btnClose.getHeight());
        btnClose.setPosition(table.getX() + table.getWidth() - btnClose.getWidth(), table.getY());
        final OptionsOverlay optionsOverlay = this;
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionsOverlay.dispose();
            }
        });

        currentScreen.stage.addActor(table);
        currentScreen.stage.addActor(btnClose);
    }

    private void submitUsername() {
        String name = textFieldName.getText().length() > 0 ? textFieldName.getText() : Constants.LANGUAGE_STRINGS.get("yourname");
        textFieldName.setText(name);
        prefs.putString(Constants.PREFS_USERNAME, name );
        prefs.flush();
        McGame.AOI.toast(Constants.LANGUAGE_STRINGS.get("saved"));
        Gdx.input.setOnscreenKeyboardVisible(false);
    }

    public void dispose() {
        table.remove();
        btnClose.remove();
    }
}
