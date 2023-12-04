package com.mygdx.platformer.Tools;

import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.platformer.Tools.Constants.PPM;

public class BodyHelperService {

    public static Body createBody(float x, float y, float width, float height,
                                  boolean isStatic, World world){

        //Declares a body definition to be used in body creation
        BodyDef bodyDef = new BodyDef();
        //Sets if the body definition is static or not
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        //Sets the position of the body definition, this should be the center of the body
        bodyDef.position.set(x / PPM, y / PPM);
        //may want to change later
        bodyDef.fixedRotation = false;
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }
}
