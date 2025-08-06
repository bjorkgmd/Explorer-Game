package com.mygdx.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Creates a texture based on a sprite sheet.
 * 
 * @author Micah B
 * @version 1.1
 */
public class TextureHelperService {

    public static TextureRegion[] extractFrames(Texture sheet, int frameWidth, int frameHeight, int columns, int row, int spacing) {
        TextureRegion[] frames = new TextureRegion[columns];

        for (int i = 0; i < columns; i++) {
            int x = i * (frameWidth + 2 * spacing) + spacing;
            int y = row * (frameHeight + 2 * spacing) + spacing;
            frames[i] = new TextureRegion(sheet, x, y, frameWidth, frameHeight);
        }

        return frames;
    }
}