package com.mygdx.helper;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.game.GameScreen;
import static com.mygdx.helper.Constants.PPM;
import com.mygdx.objects.LevelEnd;
import com.mygdx.objects.LevelStart;
import com.mygdx.objects.Player;

/**
 * Processes the map information to be used in the game.
 * 
 * @author Micah B
 * @version 1.1 
 */
public class TileMapHelper {

    private TiledMap tiledMap;
    private GameScreen gameScreen;

    /**
     * Constructor for the tileMapHelper.
     */
    public TileMapHelper(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Creates a render of the map based on the tmx file.
     */
    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("map0.tmx");
        parsemapObject(tiledMap.getLayers().get("Objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    /**
     * Process the objects that are part of a map.
     */
    private void parsemapObject(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                createStaticBody((PolygonMapObject) mapObject);
            }

            if (mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();

                if (rectangleName.equals("player")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2, 
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(), rectangle.getHeight(),
                        false, gameScreen.getWorld(), true);
                    Player player = new Player(rectangle.getWidth(), rectangle.getHeight(), body);
                    player.initialize(); // Initialize the player after creation
                    gameScreen.setPlayer(player);
                }

                if (rectangleName.equals("end")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2, 
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(), rectangle.getHeight(),
                        true, gameScreen.getWorld(), false);
                    LevelEnd levelEnd = new LevelEnd(rectangle.getWidth(), rectangle.getHeight(), body);
                    levelEnd.initialize(); // Initialize the level end after creation
                    gameScreen.setLevelEnd(levelEnd);
                }

                if (rectangleName.equals("start")) {
                    Body body = BodyHelperService.createBody(
                        rectangle.getX() + rectangle.getWidth() / 2, 
                        rectangle.getY() + rectangle.getHeight() / 2,
                        rectangle.getWidth(), rectangle.getHeight(),
                        true, gameScreen.getWorld(), false);
                    LevelStart levelStart = new LevelStart(rectangle.getWidth(), rectangle.getHeight(), body);
                    levelStart.initialize(); // Initialize the level start after creation
                    gameScreen.setLevelStart(levelStart);
                }
            }
        }
    }

    /**
     * Creates the bodies that make up the objects of a map.
     */
    private void createStaticBody(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    /**
     * Creates the shapes that make up the objects of a map.
     */
    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
            worldVertices[i] = current;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }

    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }
}