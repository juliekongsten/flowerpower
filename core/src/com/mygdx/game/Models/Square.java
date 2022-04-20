package com.mygdx.game.Models;

import com.badlogic.gdx.math.Rectangle;

/**
 * Square class that defines a square in the game.
 */
public class Square {
    private Rectangle bounds; //where the square is placed, contains the coordinates of the left bottom corner it starts and the width and height of the square
    private boolean hasFlower = false; //if there is placed a flower on this square
    private boolean isHit = false; //if the square is hit

    public Square(int x, int y, int side){
        bounds = new Rectangle(x, y, side, side);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean hasFlower() {
        return hasFlower;
    }

    public boolean isHit() { return isHit; }

    public void setHit(boolean hit){
        isHit=hit;
    }
    public void setHasFlower(boolean flower){
        hasFlower=flower;
    }



}
