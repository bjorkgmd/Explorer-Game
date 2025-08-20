package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import static com.mygdx.helper.Constants.PPM;
import com.mygdx.helper.TextureHelperService;

/**
 * Representation of the player.
 * 
 * @author Micah B
 * @version 1.1
 */
public class Player extends GameEntity {

    private int jumpCounter;
    private float stateTime = 0f;

    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> jumpAnimation;
    Animation<TextureRegion> fallAnimation;
    Animation<TextureRegion> damageAnimation;
    Animation<TextureRegion> deathAnimation;

    private boolean facingRight = true;

    private boolean dashing = false;
    private float dashTime = 0f;
    private final float DASH_DURATION = 0.15f; // seconds
    private float dashCooldown = 0f;
    private final float DASH_COOLDOWN_DURATION = 3f; // seconds

    /**
     * Constructor for the player object.
     */
    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 10f;
        this.jumpCounter = 0;

        Texture spriteSheet = new Texture("character.png");

        this.idleAnimation = new Animation<>(0.5f,
        TextureHelperService.extractFrames(spriteSheet, 16, 16, 2, 1, 8)); // row 0, 7 frames

        this.walkAnimation = new Animation<>(0.25f,
        TextureHelperService.extractFrames(spriteSheet, 16, 16, 4, 4, 8)); // row 1, 7 frames

        this.damageAnimation = new Animation<>(0.5f,
        TextureHelperService.extractFrames(spriteSheet, 16, 16, 2, 7, 8)); // row 5, 2 frames

        this.deathAnimation = new Animation<>(0.5f,
        TextureHelperService.extractFrames(spriteSheet, 16, 16, 3, 10, 8)); // row 6, 6 frames
    }

    /**
     * Initializes the player after construction.
     * This should be called after the object is fully constructed.
     */
    public void initialize() {
        if (this.body != null) {
            this.body.setUserData(this); // Set the user data to this instance
        }
    }

    /**
     * Updates the player infromation based on inputs.
     */
    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        // Dash timer update
        if (dashing) {
            dashTime += Gdx.graphics.getDeltaTime();
            if (dashTime > DASH_DURATION) {
                dashing = false;
            }
        } else {
            dashCooldown += Gdx.graphics.getDeltaTime();
        }

        checkUserInput();
    }

    /**
     * Renders the player to the screen.
     */
    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        if (Math.abs(velX) > 0) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip only if needed
        if ((facingRight && currentFrame.isFlipX()) || (!facingRight && !currentFrame.isFlipX())) {
            currentFrame.flip(true, false);
        }

        batch.draw(currentFrame, x - width / 2f, y - height / 2f - 0.1f, width, height);
    }

    /**
     * Checks for user input and updates the player's velocity and state accordingly.
     */
    private void checkUserInput() {
        velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velX = 1;
            facingRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velX = -1;
            facingRight = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCounter < 2) {
            float force = body.getMass() * 18;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
        }

        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }

        // DASH LOGIC
        if (!dashing && Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashCooldown > DASH_COOLDOWN_DURATION) {
            float force = body.getMass() * 33;
            float dashDirection = facingRight ? 1 : -1;
            body.setLinearVelocity(0, body.getLinearVelocity().y); // stop horizontal movement
            body.applyLinearImpulse(new Vector2(force * dashDirection, 0), body.getPosition(), true);
            dashing = true;
            dashTime = 0f;
            dashCooldown = 0f;
        }

        // Only set velocity from input if NOT dashing
        if (!dashing) {
            body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
        }
    }

    /**
     * Sets the position of the player.
     * 
     * @param x The x-coordinate to set.
     * @param y The y-coordinate to set.
     */
    public void setPosition(float x, float y) {
        body.setTransform(x / PPM, y / PPM, body.getAngle());
        this.x = x;
        this.y = y;
    }
}