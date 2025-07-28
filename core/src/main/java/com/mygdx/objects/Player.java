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

    /**
     * 
     */
    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 10f;
        this.jumpCounter = 0;

        Texture spriteSheet = new Texture("knight_sheet.png");
        TextureRegion[] allFrames = TextureHelperService.extractFrames(spriteSheet, 16, 16, 14, 7, 2);

        Animation<TextureRegion> idleAnimation = new Animation<>(0.1f, allFrames[0]);
        Animation<TextureRegion> walkAnimation = new Animation<>(0.1f, allFrames[1]);
        Animation<TextureRegion> jumpAnimation = new Animation<>(0.15f, allFrames[2]);
        Animation<TextureRegion> fallAnimation = new Animation<>(0.15f, allFrames[3]);
        Animation<TextureRegion> damageAnimation = new Animation<>(0.15f, allFrames[5]);
        Animation<TextureRegion> deathAnimation = new Animation<>(0.15f, allFrames[6]);
    }

    /**
     * 
     */
    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        checkUserInput();
    }

    /**
     * 
     */
    @Override
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        // TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        // batch.draw(currentFrame, x, y, width, height); // scale if needed

        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        batch.begin();
        batch.draw(currentFrame, x, y);
        batch.end();
    }

    /**
     * 
     */
    private void checkUserInput() {
        velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) velX = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) velX = -1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCounter < 2) {
            float force = body.getMass() * 18;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
        }

        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }

        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
    }
}