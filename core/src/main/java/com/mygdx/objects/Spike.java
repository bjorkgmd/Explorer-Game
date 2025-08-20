package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Abstraction of a object in the game.
 * 
 * @author Micah B
 * @version 1.1
 */
public class Spike extends GameEntity {

    /**
     * Constructor for Spike.
     * 
     * @param width Width of the spike.
     * @param height Height of the spike.
     * @param body Body of the spike.
     */
    public Spike(float width, float height, Body body) {
        super(width, height, body);
    }

    /**
     * Initializes the Spike after construction.
     * This should be called after the object is fully constructed.
     */
    public void initialize() {
        if (this.body != null) {
            this.body.setUserData(this);
            this.body.setActive(true);
        }
    }

    /**
     * Updates the level end.
     */
    @Override
    public void update() {
    }

    /**
     * Renders the level end to the screen.
     */
    @Override
    public void render(SpriteBatch batch) {
    }
}