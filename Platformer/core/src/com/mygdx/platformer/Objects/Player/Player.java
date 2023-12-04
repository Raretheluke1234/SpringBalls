package com.mygdx.platformer.Objects.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.mygdx.platformer.Screens.PlayScreen;


import static com.mygdx.platformer.Tools.Constants.PPM;

public class Player extends GameEntity{

    private static int turn = 1;

    public Player(float width, float height, Body body, int playerNum){
        super(width, height, body);
        this.speed = 7f;
        this.coyoteTimer = 0;
        this.playerNum = playerNum;
    }

    @Override
    public void update(PlayScreen gameScreen) {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        checkUserInput(gameScreen);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private void checkUserInput(PlayScreen gameScreen){

        //can't jump fix
        if(body.getLinearVelocity().y < 0.01f && body.getLinearVelocity().y > -0.01f)
            body.setLinearVelocity(body.getLinearVelocity().x,0);

        //velocity slip
        if(body.getLinearVelocity().y == 0)
            if(velX > 0.05) velX-= velX/4; else if(velX< -0.05) velX-= velX/4; else velX = 0;


        //X velocity inputs
        if (playerNum == turn) {
            if(Gdx.input.isKeyPressed(Input.Keys.D))
                if(body.getLinearVelocity().y == 0)
                    velX+= 0.1f;
                else velX+= 0.01f;
            if(Gdx.input.isKeyPressed(Input.Keys.A))
                if(body.getLinearVelocity().y == 0)
                    velX-= 0.1f;
                else velX-= 0.01f;
        }
//        if(playerNum == turn) {
//            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//                if (body.getLinearVelocity().y == 0)
//                    velX += 0.1f;
//                else velX += 0.01f;
//            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
//                if (body.getLinearVelocity().y == 0)
//                    velX -= 0.1f;
//                else velX -= 0.01f;
//        }

        //velocity max
        int denominator = 8;
        if(velX > speed/denominator) velX = speed/denominator;
        else if(velX < -speed/denominator) velX = -speed/denominator;

        //jump
        if(playerNum == turn) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) && coyoteTimer > 0) {
                float force = body.getMass() * 8;
                body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            }
        }
//        if(playerNum == turn){
//            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && coyoteTimer > 0) {
//                float force = body.getMass() * 8;
//                body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
//            }
//        }


        //coyoteTimer logic
        if(body.getLinearVelocity().y == 0) coyoteTimer = 5;
        else coyoteTimer--;


        //joint manipulation
        if(playerNum == 1){
            DistanceJoint joint = gameScreen.getJoint();
            //switch players
//            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//                if(turn == 1) turn = 2; else turn = 1;
            //modify length
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                spaceRelease = 1;
                if(joint.getLength() < 60 / PPM)
                    joint.setLength(joint.getLength() + (1 / PPM));
                if(joint.getFrequency() > 1)
                    joint.setFrequency(joint.getFrequency() - 0.1f);
            }else{
                if(spaceRelease == 1) {
                    switchTurns();
                }
                spaceRelease = 0;
                joint.setLength(gameScreen.getJointLength());
                joint.setFrequency(gameScreen.getJointFrequency());
            }

        }


        //apply horizontal velocity
        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y);

//        System.out.println("============\n"+"Player Num: "+playerNum+"\nLinearVelocity Y:"+
//                body.getLinearVelocity().y+"\nCoyote Timer: "+coyoteTimer);

    }

    private static void switchTurns(){
        if(turn == 1) turn = 2; else turn = 1;
    }

    //gives leeway when attempting to jump while falling off a ledge
    private int coyoteTimer;
    //identifies the player
    private int playerNum;
    //detects when the player lets go of space
    //0 = not held 1 = held
    private int spaceRelease;
}
