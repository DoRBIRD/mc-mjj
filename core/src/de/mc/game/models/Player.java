package de.mc.game.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mc.game.utils.Assets;
import de.mc.game.views.GameScreen;

public class Player extends Actor {
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

    public Player() {
        super();
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

    public void draw(Batch batch, GameScreen.State gameState) {
        if (gameState == GameScreen.State.GAME_RUNNING) {
            particleEffect.getEmitters().first().setPosition(getX() + 30, getY() - 20);
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
        }
        if (Assets.assetManager.update() && shieldImage != null && hasShield()) {
            batch.draw(shieldImage, hitBox.x - widthOffSet, hitBox.y);
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
        shieldCurrentDuration = 0;
    }

    public void pickupRing() {
        ringCurrentDuration = 0;
    }

    public void resetPickups() {
        ringCurrentDuration = ringDuration;
        shieldCurrentDuration = shieldDuration;
    }


    public enum Direction {
        STRAIGHT, LEFT, RIGHT
    }
}
