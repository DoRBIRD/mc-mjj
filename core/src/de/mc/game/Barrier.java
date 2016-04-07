package de.mc.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/*
 * Created by Jennifer on 07.04.2016.
 */

public class Barrier {

    final McGame                mcGame;

    private Texture             barrierImage;       //Texture of the barriers
    private Rectangle           barrier = new Rectangle();            //barrier

    public Barrier (final McGame g){
        mcGame = g;

        //Set scaling of the barrier
        this.barrier.width = 64;                                                        //width of the barrier
        this.barrier.height = 14;                                                       //heigth of the barrier

        //Set position of Barrier
        this.barrier.x = MathUtils.random(0,this.mcGame.width -this.barrier.width);     //x position of the barrier
        this.barrier.y = this.mcGame.height;                                            //y position of the barrier

        //load texture
        //this.mcGame.assetManager.load("images/barrier.jpg", Texture.class);
    }

    //Set x position of the barrier
    public void setX(float newX){
        this.barrier.x = newX;
    }

    //Get x position of the barrier
    public float getX(){
        return this.barrier.x;
    }

    //set y position of the barrier
    public void setY(float newY){
        this.barrier.y = newY;
    }

    //Get y position of the barrier
    public float getY(){
        return this.barrier.y;
    }

    //Get Rectangle of barrier
    public Rectangle getBounding(){
        return this.barrier;
    }

    //set Texture for the barrier
    public void setTexture(Texture t){
        this.barrierImage = t;
    }

    //get Texture for the barrier
    public Texture getTexture(){
        return this.barrierImage;
    }

    //dispose image
    public void dispose(){
        this.barrierImage.dispose();
    }

}
