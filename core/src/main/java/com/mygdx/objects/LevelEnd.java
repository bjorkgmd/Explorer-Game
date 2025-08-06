package com.mygdx.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Abstraction of a object in the game.
 * 
 * @author Micah B
 * @version 1.1
 */
public class LevelEnd extends GameEntity {

    /**
     * Constructor for LevelEnd.
     * 
     * @param width Width of the level end.
     * @param height Height of the level end.
     * @param body Body of the level end.
     */
    public LevelEnd(float width, float height, Body body) {
        super(width, height, body);
    }

    /**
     * Initializes the LevelEnd after construction.
     * This should be called after the object is fully constructed.
     */
    public void initialize() {
        if (this.body != null) {
            this.body.setUserData(this); // Set the user data to this instance
        }
    }

    /**
     * Updates the level end.
     */
    @Override
    public void update() {
        // LevelEnd does not require any specific update logic.
        // This method can be overridden if needed in the future.
        // Currently, it serves as a placeholder to maintain consistency with other game entities.
        if (body != null) {
            body.setActive(true); // Ensure the body is active
        }
    }

    /**
     * Renders the level end to the screen.
     */
    @Override
    public void render(SpriteBatch batch) {
    }
}