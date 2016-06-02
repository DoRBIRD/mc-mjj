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


    public enum Direction {
        STRAIGHT, LEFT, RIGHT
    }
}
