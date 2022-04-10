package com.mygdx.game.Models;

import java.util.ArrayList;
import java.util.List;

public class Bed {
    private List<Square> squares = new ArrayList<>(); //the squares the bed consists of, the squares should have placement so that the bed is inside the pool at start

    public Bed(){

    }

    public void addSquare(Square square){
        squares.add(square);
    }


}
