package de.mc.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class SaveUserNameListener implements Input.TextInputListener {
    @Override
    public void input (String text) {
        System.out.println(text);
        Preferences preferences = Gdx.app.getPreferences(Constants.DEFAULT_PREFS);
        preferences.putString(Constants.PREFS_USERNAME, text);
        preferences.flush();
    }

    @Override
    public void canceled () {
    }
}
