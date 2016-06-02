package de.mc.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mc.game.Assets;

public class Player extends Actor {
    private Rectangle hitBox;
    private float hitBoxWidth;
    private float hitBoxHeight;
    private Texture playerImage;

    private float shieldDuration = 5;
    private float shieldCurrentDuration = shieldDuration;
    private float ringDuration = 5;
    private float ringCurrentDuration = ringDuration;

    public Player() {
        super();
        hitBox = new Rectangle();
        hitBoxWidth = 150;
        hitBoxHeight = 280;
        hitBox.width = hitBoxWidth;
        hitBox.height = hitBoxHeight;

        setBounds(0, 0, hitBoxWidth, hitBoxHeight);

        hitBox.x = getX() - hitBoxWidth / 2;
        hitBox.y = getY() - hitBoxHeight / 2;
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        hitBox.x = getX() - hitBoxWidth / 2;
        hitBox.y = getY() - hitBoxHeight / 2;
    }

    public void updateImage(Direction dir) {
        if (hasRing()) {
            switch (dir) {
                case LEFT:
                    playerImage = Assets.ringLeftTexture;
                    break;
                case RIGHT:
                    playerImage = Assets.ringRightTexture;
                    break;
                case STRAIGHT:
                    playerImage = Assets.ringStraightTexture;
                    break;
                default:
                    playerImage = Assets.ringStraightTexture;
                    break;
            }
        } else if (hasShield()) {
            switch (dir) {
                case LEFT:
                    playerImage = Assets.playerLeftTexture;
                    break;
                case RIGHT:
                    playerImage = Assets.playerRightTexture;
                    break;
                case STRAIGHT:
                    playerImage = Assets.playerStraightTexture;
                    break;
                default:
                    playerImage = Assets.playerStraightTexture;
                    break;
            }
        } else {
            switch (dir) {
                case LEFT:
                    playerImage = Assets.playerLeftTexture;
                    break;
                case RIGHT:
                    playerImage = Assets.playerRightTexture;
                    break;
                case STRAIGHT:
                    playerImage = Assets.playerStraightTexture;
                    break;
                default:
                    playerImage = Assets.playerStraightTexture;
                    break;
            }
        }
    }

    public Texture getImage() {
        return playerImage;
    }

    public void setImage(Texture newTexture) {
        playerImage = newTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Assets.assetManager.update() && playerImage != null) {
            batch.draw(playerImage, hitBox.x, hitBox.y);
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean hasRing() {
        if (ringCurrentDuration < ringDuration) return true;
        return false;
    }

    public boolean hasShield() {
        if (shieldCurrentDuration < shieldDuration) return true;
        return false;
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

    public enum Direction {
        STRAIGHT, LEFT, RIGHT
    }
}
