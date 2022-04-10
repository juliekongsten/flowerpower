package com.mygdx.game.Controllers;

import com.mygdx.game.Models.Square;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    //har per nå laget kun en liste, kan heller ha liste i liste for å lettere navigere seg opp/ned/sidelengs men har ikke det nå
    private List<Square> opBoard = new ArrayList<>();
    private List<Square> myBoard = new ArrayList<>();
    private int squaresize;
    private int numberSquaresHeight;
    private int numberSquaresWidth;

    /* OBS: har foreløpig satt de rutene i motstander slik at de tre nederste rekkene har blomster
            og de øverste ikke har det. Er for å teste at ting fungerer slik det skal.
            De skal senere settes ut fra hvor motstanderen har plassert beds.
    * */
    public GameController(){
        //tenker her at vi kan ha satt tall for de forskjellige vanskelighetsgradene
        squaresize = 32;
        numberSquaresHeight = 6;
        numberSquaresWidth = 9;

        //må hente x og y-verdier fra view heller sånn at vi får riktige :D
        //henter nå fra printsetting i gameview, er nok lurt å gjøre det mindre hardkoding
        int x = 26+15;
        int my_y = 65+12;
        int op_y = 424+12;
        for (int i = 0; i< numberSquaresHeight; i++){
            for (int j = 0; j< numberSquaresWidth; j++){
                myBoard.add(new Square(x, my_y, squaresize));
                Square opSquare = new Square(x,op_y,squaresize);
                if (i<3){
                    opSquare.setHasFlower(true);
                }
                opBoard.add(opSquare);
                x+=squaresize;

            }
            x = 26+15;
            my_y+=squaresize;
            op_y+=squaresize;
        }

    }

    public List<Square> getOpBoard(){
        return opBoard;
    }

    public List<Square> getMyBoard(){
        return myBoard;
    }

    /**
     * Method for when the player hits a square on opponents board.
     * @param square that is hit
     * return if the hit square contains a flower
     */
    public boolean hitSquare(Square square){
        //should probably also have logic that updates "myboard" for the opponent!
        if (!opBoard.contains(square)){
            return false;
        }
        square.setHit(true);
        return square.hasFlower();
    }

}
