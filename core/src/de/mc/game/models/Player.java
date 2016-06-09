package de.mc.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.mc.game.utils.Assets;
import de.mc.game.utils.Constants;
import de.mc.game.utils.CustomProgressBar;
import de.mc.game.views.State;

public class Player extends Actor {
    boolean onlyOnePickUp = false;
    private Texture playerImage;
    private Texture ringImage;
    private Texture shieldImage;
    private Rectangle hitBox;
    private float hitBoxWidth;
    private float hitBoxHeight;
    private float widthOffSet = 20;
    private float shieldDuration = 5;
    private float shieldCurrentDuration = shieldDuration;
    private float ringDuration = 5;
    private float ringCurrentDuration = ringDuration;
    private ParticleEffect particleEffect;
    private final CustomProgressBar progressBarRing, progressBarShield;

    public Player(Stage stage) {
        super();

        setStage(stage);

        hitBox = new Rectangle();

        hitBoxWidth = 150 - widthOffSet * 2;
        hitBoxHeight = 280;
        hitBox.width = hitBoxWidth;
        hitBox.height = hitBoxHeight;

        setBounds(0, 0, hitBoxWidth, hitBoxHeight);

        hitBox.x = getX() - hitBoxWidth / 2;
        hitBox.y = getY() - hitBoxHeight / 2;


        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/snow.particle"), Gdx.files.internal("particles"));
        particleEffect.getEmitters().first().setPosition(getX(), getY());
        particleEffect.start();

        progressBarRing = new CustomProgressBar(getStage(), 0, ringDuration, 0.0000001f, false, Assets.defaultProgressBarStyle);
        progressBarRing.setWidth(Constants.WIDTH - 100);
        progressBarRing.setPosition(Constants.WIDTH / 2 - progressBarRing.getWidth() / 2, 100);
        progressBarRing.setValue(progressBarRing.getMaxValue());

        Skin progressBarSkin = new Skin(new TextureAtlas("progressbar/default-progress-bar.pack"));
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle(Assets.defaultProgressBarStyle);
        style.knobBefore = progressBarSkin.getDrawable("progress_bar_knob_before_blue");
        style.knob = progressBarSkin.getDrawable("progress_bar_knob_right_blue");
        progressBarShield = new CustomProgressBar(getStage(), 0, shieldDuration, 0.0000001f, false, style);
        progressBarShield.setWidth(Constants.WIDTH - 100);
        progressBarShield.setKnobLeftImage(progressBarSkin.getDrawable("progress_bar_knob_left_blue"));
        progressBarShield.setPosition(Constants.WIDTH / 2 - progressBarShield.getWidth() / 2, 150);
        progressBarShield.setValue(progressBarShield.getMaxValue());

        getStage().addActor(progressBarRing);
        getStage().addActor(progressBarShield);
    }

    public float getRingCurrentDuration() {
        return ringCurrentDuration;
    }

    public float getRingDuration() {
        return ringDuration;
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        hitBox.x = getX() - hitBoxWidth / 2;
        hitBox.y = getY() - hitBoxHeight / 2;
    }

    public void updateImage(Direction dir) {
        switch (dir) {
            case LEFT:
                ringImage = Assets.ringLeftTexture;
                shieldImage = Assets.shieldLeftTexture;
                playerImage = Assets.playerLeftTexture;
                break;
            case RIGHT:
                ringImage = Assets.ringRightTexture;
                shieldImage = Assets.shieldRightTexture;
                playerImage = Assets.playerRightTexture;
                break;
            case STRAIGHT:
                ringImage = Assets.ringStraightTexture;
                shieldImage = Assets.shieldStraightTexture;
                playerImage = Assets.playerStraightTexture;
                break;
            default:
                ringImage = Assets.ringStraightTexture;
                shieldImage = Assets.shieldStraightTexture;
                playerImage = Assets.playerStraightTexture;
                break;
        }
    }

    public Texture getImage() {
        return playerImage;
    }

    public void setImage(Texture newTexture) {
        playerImage = newTexture;
    }

    public void draw(Batch batch, State gameState) {
        particleEffect.getEmitters().first().setPosition(getX() + 30, getY() - 20);
        if (gameState == State.GAME_RUNNING) {
            particleEffect.update(Gdx.graphics.getDeltaTime());
            particleEffect.draw(batch);
            if (particleEffect.isComplete())
                particleEffect.reset();
        }
        if (Assets.assetManager.update() && playerImage != null) {
            batch.draw(playerImage, hitBox.x - widthOffSet, hitBox.y);
        }
        if (Assets.assetManager.update() && ringImage != null && hasRing()) {
            batch.draw(ringImage, hitBox.x - widthOffSet, hitBox.y);
            progressBarRing.setValue(ringCurrentDuration < progressBarRing.getMaxValue() ? ringDuration - ringCurrentDuration : progressBarRing.getMaxValue());
        } else {
            progressBarRing.setVisible(false);
        }
        if (Assets.assetManager.update() && shieldImage != null && hasShield()) {
            batch.draw(shieldImage, hitBox.x - widthOffSet, hitBox.y);
            progressBarShield.setValue(shieldCurrentDuration < progressBarShield.getMaxValue() ? shieldDuration - shieldCurrentDuration : progressBarShield.getMaxValue());
        } else {
            progressBarShield.setVisible(false);
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean hasRing() {
        return ringCurrentDuration < ringDuration;
    }

    public boolean hasShield() {
        return shieldCurrentDuration < shieldDuration;
    }

    public void updatePowerUpsTimer(float delta) {
        ringCurrentDuration += delta;
        shieldCurrentDuration += delta;
    }

    public void pickupShield() {
        if (onlyOnePickUp) resetPickups();
        shieldCurrentDuration = 0;
        progressBarShield.setVisible(true);
    }

    public void pickupRing() {
        if (onlyOnePickUp) resetPickups();
        ringCurrentDuration = 0;
        progressBarRing.setVisible(true);
    }

    public void resetPickups() {
        ringCurrentDuration = ringDuration;
        shieldCurrentDuration = shieldDuration;
    }

    public enum Direction {
        STRAIGHT, LEFT, RIGHT
    }
}
