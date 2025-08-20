package com.mygdx.helper;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static com.mygdx.helper.Constants.PPM;

/**
 * Creates the body for any object.
 * 
 * @author Micah B
 * @version 1.1
 */
public class BodyHelperService {

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, World world, boolean collision) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM - 0.1f, height / 2 / PPM - 0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = !collision;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }
}