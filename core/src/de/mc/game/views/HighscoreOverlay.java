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

import java.text.DecimalFormat;
import java.util.List;

import de.mc.game.models.Database.HighscoreDAO;
import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;

public class HighscoreOverlay {

    private final Button btnClose = new Button(Assets.menuCloseButtonStyle);
    private GameScreen gameScreen;
    private Table table = new Table();
    private TutorialScreen tutorialScreen;
    private HighscoreDAO dbScores = new HighscoreDAO();

    public HighscoreOverlay(GameScreen gs, TutorialScreen ts) {
        gameScreen = gs;
        tutorialScreen = ts;

        if(getScores().size() != 0) {

            Image background = new Image(Assets.menuTitleButtonLargeTexture);
            background.setWidth(background.getWidth() * Constants.SCALING);
            background.setHeight(background.getHeight() * Constants.SCALING);

            Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.WHITE);
            final Label labelTitle = new Label("Highscore", labelStyle);
            labelTitle.setAlignment(Align.center);

            labelStyle = new Label.LabelStyle(Assets.TONDU_BETA, Color.BLACK);


            table = new Table();
            table.setBackground(background.getDrawable());
            table.setWidth(background.getWidth() * 0.95f);
            table.setHeight(background.getHeight());
            table.setPosition(Constants.WIDTH / 2 - table.getWidth() / 2, Constants.HEIGHT / 2 - table.getHeight() / 2);
            table.defaults()
                    .prefHeight(Value.percentHeight(1.1f))
                    .prefWidth(table.getWidth() * 0.5f)
                    .minHeight(Value.minHeight);
            table.add(labelTitle)
                    .padBottom(100)
                    .colspan(2);

            for (String score : getScores()) {
                DecimalFormat df = new DecimalFormat("#.#");
                String[] splittedScore = score.split(": ");
                final Label nameLabel = new Label(splittedScore[0], labelStyle);
                nameLabel.setAlignment(Align.right);
                final Label scoreLabel = new Label(df.format(Float.parseFloat(splittedScore[1])), labelStyle);
                table.row();
                table.add(nameLabel)
                        .prefWidth(table.getWidth() * 0.4f)
                        .maxWidth(table.getWidth() * 0.4f);
                table.add(scoreLabel)
                        .prefWidth(table.getWidth() * 0.3f)
                        .padLeft(100);
            }

            table.padBottom(65);
        }
        btnClose.setWidth(btnClose.getWidth());
        btnClose.setHeight(btnClose.getHeight());
        btnClose.setPosition(table.getX() + table.getWidth() - btnClose.getWidth(), table.getY());
        final HighscoreOverlay highscoreOverlay = this;
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                highscoreOverlay.dispose();
            }
        });
        if (gameScreen != null) {
            gameScreen.stage.addActor(table);
            gameScreen.stage.addActor(btnClose);
        } else if (tutorialScreen != null) {
            tutorialScreen.stage.addActor(table);
            tutorialScreen.stage.addActor(btnClose);
        }
    }

    private List<String> getScores() {
        return this.dbScores.getScores();
    }

    public void dispose() {
        table.remove();
        btnClose.remove();
    }
}
