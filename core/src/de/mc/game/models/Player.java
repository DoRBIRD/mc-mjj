package de.mc.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mc.game.Constants;
import de.mc.game.McGame;

import static de.mc.game.models.Player.Direction.*;

public class Player extends Actor {
    private Rectangle hitBox;
    private float hitBoxWidth;
    private float hitBoxHeight;
    private Texture playerImage;
    private Vector2 position;
    private McGame mcGame;

    public Player(final McGame g) {
        super();
        mcGame = g;
        hitBox = new Rectangle();
        position = new Vector2(Constants.MAP_WIDTH/2,0);

        hitBoxWidth=44;
        hitBoxHeight=56;
        updateHitBox();
        hitBox.width = hitBoxWidth;
        hitBox.height = hitBoxWidth;
        mcGame.assetManager.load("images/player_normal_b.png", Texture.class);
        mcGame.assetManager.load("images/player_left_b.png", Texture.class);
        mcGame.assetManager.load("images/player_right_b.png", Texture.class);
        //playerImage = mcGame.assetManager.get("images/player_normal_b.png", Texture.class);
    }

    private void updateHitBox(){
        hitBox.x = position.x - hitBoxWidth / 2;
        hitBox.y = position.y - hitBoxHeight / 2;
    }

    public void setPositionX(float x){
        setPosition(new Vector2(x,getPosition().y));
    }

    public void setPositionY(float y){
        setPosition(new Vector2(getPosition().x,y));
    }

    public void setPosition(float x,float y){
        setPosition(new Vector2(x,y));
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(Vector2 newPosition){
        position = newPosition;
        updateHitBox();
    }

    public void translate(Vector2 delta){
        position.x += delta.x;
        position.y += delta.y;
        updateHitBox();
    }

    public void updateImage(Direction dir){
        switch (dir){
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

    public Texture getImage(){
        return playerImage;
    }

    public void setImage(Texture newTexture){
        playerImage=newTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(mcGame.assetManager.update()) {mcGame.batch.draw(playerImage, hitBox.x, hitBox.y);}
    }

    public Rectangle getHitBox() {
        return hitBox;
    }


    public enum Direction {
        STRAIGHT, LEFT, RIGHT
    }
}
