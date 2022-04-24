package com.mygdx.game.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Button {

    private Texture texture;
    private TextureRegion texReg;
    private TextureRegionDrawable texRegDraw;
    private ImageButton imageButton;
    float pos_x;
    float pos_y;

    public Button(String texturePath, float pos_x, float pos_y) {
        texture = new Texture(texturePath);
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        initButton();
    }

    private void initButton() {
        texReg = new TextureRegion(texture);
        texRegDraw = new TextureRegionDrawable(texReg);
        imageButton = new ImageButton(texRegDraw);
        imageButton.setPosition(pos_x, pos_y);
    }

    public ImageButton getButton() {
        return imageButton;
    }
}
