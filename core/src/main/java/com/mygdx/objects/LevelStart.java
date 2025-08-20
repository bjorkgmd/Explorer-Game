package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import static com.mygdx.helper.Constants.PPM;

/**
 * Abstraction of a object in the game.
 * 
 * @author Micah B
 * @version 1.1
 */
public class LevelStart extends GameEntity {

    /**
     * Constructor for LevelStart.
     * 
     * @param width Width of the level start.
     * @param height Height of the level start.
     * @param body Body of the level start.
     */
    public LevelStart(float width, float height, Body body) {
        super(width, height, body);
    }

    /**
     * Initializes the LevelStart after construction.
     * This should be called after the object is fully constructed.
     */
    public void initialize() {
        if (this.body != null) {
            this.body.setUserData(this);
            this.body.setActive(true);
        }
    }

    /**
     * Updates the level start.
     */
    @Override
    public void update() {
    }

    /**
     * Renders the level start to the screen.
     */
    @Override
    public void render(SpriteBatch batch) {
    }
    
    /**
     * Returns the position of the level start.
     */
    public float getX() {
        return body.getPosition().x * PPM;
    }

    /**
     * Returns the position of the level start.
     */
    public float getY() {
        return body.getPosition().y * PPM;
    }
}