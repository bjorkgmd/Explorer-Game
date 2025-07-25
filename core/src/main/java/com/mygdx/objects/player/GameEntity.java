package com.mygdx.objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Abstraction of a object in the game, I think?
 * 
 * @author Micah B
 * @version 1.1
 */
public abstract class GameEntity {

    protected float x, y, velX, velY, speed;
    protected float width, height;
    protected Body body;

    public GameEntity(float width, float height, Body body) {
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.width = width;
        this.height = height;
        this.body = body;
        this.velX = 0;
        this.velY = 0;
        this.speed = 0;
    }

    public abstract void update();

    public abstract void render(SpriteBatch batch);

    public Body getBody() {
        return body;
    }
}