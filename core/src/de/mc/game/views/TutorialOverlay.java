package de.mc.game.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;

public class TutorialOverlay {

    private final Button btnClose;

    private TutorialScreen tutorialScreen;
    private Table table;

    public TutorialOverlay(TutorialScreen ts, String tipp) {

        tutorialScreen = ts;

        Image background = new Image(Assets.menuTitleButtonTexture);
        background.setWidth(background.getWidth() * Constants.SCALING);
        background.setHeight(background.getHeight() * Constants.SCALING);

        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);
        final Label labelTitle = new Label("Tipp", labelStyle);
        labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.BLACK);
        final Label labelTipp1 = new Label(tipp, labelStyle);
        labelTipp1.setWrap(true);
        labelTipp1.setWidth(200);
        labelTitle.setAlignment(Align.center);

        final Label labelTipp2 = new Label(" ", labelStyle);
        labelTitle.setAlignment(Align.center);


        table = new Table();
        table.setBackground(background.getDrawable());
        table.setWidth(background.getWidth() * 0.95f);
        table.setHeight(background.getHeight());
        table.setPosition(Constants.WIDTH / 2 - table.getWidth() / 2, Constants.HEIGHT / 2 - table.getHeight() / 2);
        table.defaults()
                .minWidth(Value.percentWidth(1.2f))
                .prefWidth(table.getWidth() * 0.5f)
                .minHeight(Value.minHeight)
                .prefHeight(Value.percentHeight(1.2f));
        table.add(labelTitle)
                .padBottom(50);
        table.row();
        table.add(labelTipp1)
                .padBottom(50);
        table.row();
        table.add(labelTipp2)
                .padBottom(300);

        btnClose = new Button(Assets.menuCloseButtonStyle);
        btnClose.setWidth(btnClose.getWidth());
        btnClose.setHeight(btnClose.getHeight());
        btnClose.setPosition(table.getX() + table.getWidth() - btnClose.getWidth(), table.getY());
        final TutorialOverlay tutorialOverlay = this;
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tutorialScreen.setReady();
                tutorialOverlay.dispose();
            }
        });
        tutorialScreen.stage.addActor(table);
        tutorialScreen.stage.addActor(btnClose);
    }

    public void dispose() {
        table.remove();
        btnClose.remove();
    }
}
