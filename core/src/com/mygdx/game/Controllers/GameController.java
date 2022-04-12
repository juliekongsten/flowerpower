package com.mygdx.game.Controllers;

import com.mygdx.game.Models.Bed;
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
    private int numberBeds = 5;
    private List<Bed> myBeds = new ArrayList<>();
    private List<Bed> opBeds = new ArrayList<>();

    /* OBS: har foreløpig satt de rutene i motstander slik at de tre nederste rekkene har blomster
            og de øverste ikke har det. Er for å teste at ting fungerer slik det skal.
            De skal senere settes ut fra hvor motstanderen har plassert beds.
    * */
    public GameController(){
        //tenker her at vi kan ha satt tall for de forskjellige vanskelighetsgradene
        squaresize = 32;
        numberSquaresHeight = 6;
        numberSquaresWidth = 9;

        //TODO: Get x- and y-values without hardkoding :D
        //må hente x og y-verdier fra view heller sånn at vi får riktige :D
        //henter nå fra printsetting i gameview, er nok lurt å gjøre det mindre hardkoding
        int x = 26+15;
        int my_y = 65+12;
        int op_y = 424+12;
        for (int i = 0; i< numberSquaresHeight; i++){
            for (int j = 0; j< numberSquaresWidth; j++){
                myBoard.add(new Square(x, my_y, squaresize));
                Square opSquare = new Square(x,op_y,squaresize); //should find somewhere
                if (i<3){
                    opSquare.setHasFlower(true); //we probably don't need this as this should be taken care of by opponent and taken care of somewhere else?
                }
                opBoard.add(opSquare);
                x+=squaresize;

            }
            x = 26+15;
            my_y+=squaresize;
            op_y+=squaresize;
        }
        setStartBeds();
    }

    public List<Square> getOpBoard(){ return opBoard; }
    public List<Square> getMyBoard(){ return myBoard; }
    public List<Bed> getMyBeds() { return myBeds; }
    public List<Bed> getOpBeds() { return opBeds; }



    /**
     * Method for when the player hits a square on opponents board.
     * @param square that is hit
     * return if the hit square contains a flower
     */
    public boolean hitSquare(Square square){
        //should probably also have logic that updates "myboard" for the opponent!
        //TODO: find logic to update opponent as well
        if (!opBoard.contains(square)){
            return false;
        }
        square.setHit(true);
        return square.hasFlower();
    }

    public void setStartBeds(){
        //TODO: find logic
        //should make sure that startbeds are the same for both players
        //might need some argument
        //sets all beds inside pool
        Bed bed1 = new Bed(3, true, "flowerbed_1.png");
        Bed bed2 = new Bed(4, true, "flowerbed_2.png");
        Bed bed3 = new Bed(3, false, "flowerbed_3.png");
        Bed bed4 = new Bed(2, false, "flowerbed_4.png");
        Bed bed5 = new Bed(5, true, "flowerbed_5.png");
        myBeds.add(bed1);
        myBeds.add(bed2);
        myBeds.add(bed3);
        myBeds.add(bed4);
        myBeds.add(bed5);

    }

    public void setOpBeds(List<Bed> beds){
        //TODO: find logic
        //should set OpBeds to be the beds that opponent has placed
    }



}
