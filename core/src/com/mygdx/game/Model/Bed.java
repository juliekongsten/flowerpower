package com.mygdx.game.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Bed class that defines a flowerbed in the game.
 * A bed is defined by a size, (number of squares it covers), direction (if it is horizontal or
 * vertical) and a texturepath (for graphics)
 * A bed can only be rectangular.
 * A bed is initially always placed in origo, but can be moved.
 */
public class Bed {
    private int size; //Number of squares this bed covers
    private boolean horizontal;
    private Texture bed;
    private Rectangle bounds; //Defines the space this bed covers
    private float pos_x = 0;
    private float pos_y = 0;
    private String texturePath;


    public Bed() {

    }

    public Bed(int size, boolean horizontal, String texturePath){
        this.size = size;
        this.horizontal = horizontal;
        bed = new Texture(texturePath);
        this.texturePath = texturePath;
    }

    /**
     * Returns if the bed is fully hit given board it covers
     * @param board
     * @return
     */
    public boolean isFullyHit(List<Square> board){
        int hitSquares = 0;
        for (Square square : getSquares(board)){
            if (square.isHit()){
                hitSquares++;
            }
        }
        if (hitSquares==size){
            return true;
        }
        return false;
    }

    public String getTexturePath(){ return texturePath; }
    public int getSize() { return size; }
    public Rectangle getBounds() { return bounds; }
    public float getPos_x() { return pos_x; }
    public float getPos_y() { return pos_y; }
    public Texture getTexture() { return bed; }
    public boolean isHorizontal() { return horizontal; }

    private void setBounds(float x, float y) {
        bounds = new Rectangle(x, y, bed.getWidth(), bed.getHeight());
    }

    /**
     * Moves the bed to given coordinates
     * @param x x-position the bed should be placed
     * @param y y-position the bed should be placed
     */
    public void updatePosition(float x, float y) {
        this.pos_x = x;
        this.pos_y = y;
        setBounds(x, y);
    }

    /**
     * Returns the squares of a board that this bed covers.
     * @param squareBoard Board that is covered
     * @return List of squares that the bed covers. Empty list if this bed does not cover any of the
     * board's squares.
     */
    public List<Square> getSquares(List<Square> squareBoard){
        List<Square> squares = new ArrayList<>();

        for (Square square : squareBoard){
            if (bounds.contains(square.getBounds().x +2, square.getBounds().y +2)){
                squares.add(square);
            }
        }

        return squares;
    }

    /**
     * Moves position to bed to fit in a squareboard
     * @param squareBoard
     */
    public void moveToNearestSquares(List<Square> squareBoard){
        Square s = null;
        for (Square square : squareBoard){
            //TODO: (low priority) Try to find way to more exact movements
            if (!horizontal){
                if (square.getBounds().contains(pos_x+ getTexture().getWidth()/2,pos_y)){
                    s = square;
                }
            } else {
                if (square.getBounds().contains(pos_x,pos_y+getTexture().getHeight()/2)){
                    s = square;
                }
            }

        }
        if (s!=null){
            updatePosition(s.getBounds().x, s.getBounds().y);
        }
    }

    @Override
    public String toString() {
        final StringBuilder build = new StringBuilder("Bed{");
        build.append("size='").append(size).append('\'');
        build.append(", horizontal='").append(horizontal).append('\'');
        build.append(", bed='").append(bed).append('\'');
        build.append(", bounds='").append(bounds).append('\'');
        build.append(", pos_x='").append(pos_x).append('\'');
        build.append(", pos_y='").append(pos_y).append('\'');
        build.append(", texturePath='").append(texturePath).append('\'');
        build.append('}');
        return build.toString();
    }

}
