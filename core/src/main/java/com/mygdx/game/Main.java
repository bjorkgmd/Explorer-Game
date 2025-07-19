package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/** 
 * Creates the actual game instance, I think?
 * @author Micah B
 * @version 1.1
 */
public class Main extends Game {

    public static Main Instance;
    private int widthScreen, heightScreen;
    private OrthographicCamera camera;

    public Main() {
        Instance = this;
    }

    @Override
    public void create() {
        this.widthScreen = Gdx.graphics.getWidth();
        this.heightScreen = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera(); 
        this.camera.setToOrtho(false, widthScreen, heightScreen);
        setScreen(new GameScreen(camera));
    }

}
