package com.mygdx.game.Models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Bed {
    private int size; // Antall ruter man har plass til i en bed
    private boolean horizontal;
    private Texture bed;
    private Rectangle bounds;
    private float pos_x = 0;
    private float pos_y = 0;
    private String texturePath;

    //TODO: alt:)

    public Bed(int size, boolean horizontal, String texturePath){
        this.size = size;
        this.horizontal = horizontal;
        bed = new Texture(texturePath);
        this.texturePath = texturePath;
    }

    public String getTexturePath(){
        return texturePath;
    }

    public int getSize() {
        return size;
    }

    public Texture getBed() {
        return bed;
    }

    private void setBounds(float x, float y) {
        bounds = new Rectangle(x, y, bed.getWidth(), bed.getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void updatePosition(float x, float y) {
        this.pos_x = x;
        this.pos_y = y;
        setBounds(x, y);
    }

    public float getPos_x() {
        return pos_x;
    }

    public float getPos_y() {
        return pos_y;
    }

    public Texture getTexture() {
        return bed;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public List<Square> getSquares(List<Square> squareBoard){
        List<Square> squares = new ArrayList<>();

        for (Square square : squareBoard){
            if (bounds.contains(square.getBounds().x +2, square.getBounds().y +2)){
                squares.add(square);
            }
        }

        return squares;
    }

}
