package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import static com.mygdx.helper.Constants.PPM;
import com.mygdx.helper.TileMapHelper;
import com.mygdx.objects.LevelEnd;
import com.mygdx.objects.LevelStart;
import com.mygdx.objects.Player;
import com.mygdx.objects.Spike;

/**
 * Visual component of the game, I think?
 * 
 * @author Micah B
 * @version 1.1 
 */
public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer Box2DDebugRenderer;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private Player player;
    private Spike spike;
    private LevelEnd levelEnd;
    private LevelStart levelStart;

    private Texture background1Texture;
    private Texture background2Texture;

    private int currentMapIndex = 0;

    /**
     * Constructor for GameScreen. 
     */
    public GameScreen(OrthographicCamera camera) {
        background1Texture = new Texture("Background_1.png");
        background2Texture = new Texture("Background_2.png");
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -25f), false);
        this.Box2DDebugRenderer = new Box2DDebugRenderer();

        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
        
        camera.zoom = 0.5f; // 0.5 = 2x bigger, 2.0 = 2x smaller

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();

                if (a == null || b == null) return;

                // Check for collision between player and levelEnd
                if ((a == player && b == levelEnd) || (a == levelEnd && b == player)) {
                    // currentMapIndex++;
                    loadMap("maps/map" + currentMapIndex + ".tmx");
                }
                if ((a == player && b == spike) || (a == spike && b == player)) {
                    player.setPosition(levelStart.getX(), levelStart.getY());
                }
            }
            @Override public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {}
            @Override public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, com.badlogic.gdx.physics.box2d.Manifold oldManifold) {}
            @Override public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, com.badlogic.gdx.physics.box2d.ContactImpulse impulse) {}
        });
    }

    /**
     * 
     */
    private void update() {
        world.step(1 / 60f, 6, 2);
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        // if () {
        //     loadMap("maps/map" + currentMapIndex + ".tmx");
        // }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

    }

    /**
     * 
     */
    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
    }

    /**
     * 
     */
    @Override
    public void render(float delta) {
        this.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(
            background1Texture,
            camera.position.x - camera.viewportWidth * 0.5f * camera.zoom,
            camera.position.y - camera.viewportHeight * 0.5f * camera.zoom,
            camera.viewportWidth * camera.zoom,
            camera.viewportHeight * camera.zoom
        );
        batch.draw(
            background2Texture,
            camera.position.x - camera.viewportWidth * 0.5f * camera.zoom,
            camera.position.y - camera.viewportHeight * 0.5f * camera.zoom,
            camera.viewportWidth * camera.zoom,
            camera.viewportHeight * camera.zoom
        );
        batch.end();

        orthogonalTiledMapRenderer.render();

        batch.begin();
        player.render(batch);
        batch.end();
        Box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLevelEnd(LevelEnd levelEnd) {
        this.levelEnd = levelEnd;
    }

    public void setLevelStart(LevelStart levelStart) {
        this.levelStart = levelStart;
    }

    private void loadMap(String string) {
        // 1. Dispose the current tiled map and renderer
        // orthogonalTiledMapRenderer.getMap().dispose();
        // orthogonalTiledMapRenderer.dispose();
        this.tileMapHelper.dispose();

        // // 2. Destroy all bodies in the world (don't dispose the world itself)
        // Array<Body> bodies = new Array<Body>();
        // world.getBodies(bodies);
        // for (Body body : bodies) {
        //     world.destroyBody(body);
        // }

        // // 3. Recreate map + renderer
        // orthogonalTiledMapRenderer = tileMapHelper.setupMap(); // Use new map path as needed
    }
}