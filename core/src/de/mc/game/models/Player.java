package de.mc.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mc.game.McGame;

public class Player extends Actor {
    private Rectangle hitBox;
    private float hitBoxWidth;
    private float hitBoxHeight;
    private Texture playerImage;
    private McGame mcGame;

    public Player(final McGame g) {
        super();
        mcGame = g;
        hitBox = new Rectangle();
        hitBoxWidth = 107;
        hitBoxHeight = 159;
        hitBox.width = hitBoxWidth;
        hitBox.height = hitBoxHeight;

        setBounds(0, 0, hitBoxWidth, hitBoxHeight);

        hitBox.x = getX();
        hitBox.y = getY();

        mcGame.assetManager.load("images/player.gif", Texture.class);
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
                playerImage = mcGame.assetManager.get("images/player.gif", Texture.class);
                break;
            case RIGHT:
                playerImage = mcGame.assetManager.get("images/player.gif", Texture.class);
                break;
            case STRAIGHT:
                playerImage = mcGame.assetManager.get("images/player.gif", Texture.class);
                break;
            default:
                playerImage = mcGame.assetManager.get("images/player.gif", Texture.class);
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
        if (mcGame.assetManager.update() && playerImage != null) {
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
