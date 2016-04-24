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
        hitBoxWidth = 44;
        hitBoxHeight = 56;
        hitBox.width = hitBoxWidth;
        hitBox.height = hitBoxHeight;

        setBounds(0, 0, hitBoxWidth, hitBoxHeight);

        hitBox.x = getX();
        hitBox.y = getY();

        mcGame.assetManager.load("images/player_normal_b.png", Texture.class);
        mcGame.assetManager.load("images/player_left_b.png", Texture.class);
        mcGame.assetManager.load("images/player_right_b.png", Texture.class);
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
                playerImage = mcGame.assetManager.get("images/player_left_b.png", Texture.class);
                break;
            case RIGHT:
                playerImage = mcGame.assetManager.get("images/player_right_b.png", Texture.class);
                break;
            case STRAIGHT:
                playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
                break;
            default:
                playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
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
