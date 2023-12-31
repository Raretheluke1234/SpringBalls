package com.mygdx.platformer.Objects.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.platformer.Screens.PlayScreen;

public abstract class GameEntity {
    protected float x,y,velX,velY,speed,torque;
    protected float width, height;
    protected Body body;
    
    public GameEntity(float width, float height, Body body){
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.width = width;
        this.height = height;
        this.velX = 0;
        this.velY = 0;
        this.torque = 0;
        this.speed = 0;
        this.body = body;
    }
    
    public abstract void update(PlayScreen gameScreen);
    
    public abstract void render(SpriteBatch batch);

    public Body getBody(){
        return body;
    }
}
