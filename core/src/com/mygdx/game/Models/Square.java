package com.mygdx.game.Models;

import com.badlogic.gdx.math.Rectangle;

public class Square {
    private Rectangle bounds;
    private boolean hasFlower = false; //tenker vi alltid starter med false så oppdateres når det plasseres:)
    private boolean isHit = false;
    //evt ha noe for om den er del av en fulltruffet bed, men det kan nok taes hånd om typ i bed-klassen slik at alle disse oppdateres til "full" ??
    //må også håndtere hvor de er for å sørge for riktig grafikk når det er fullt bed, men kan nok også gjøres annet sted

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
