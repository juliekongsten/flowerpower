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

    private int distance = 359;

    private boolean gameOver = false;
    private boolean won = false;

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
        setMyBeds(null);
        List<Bed> sentOpBeds = new ArrayList<>(); //TODO: Get this from other player
        setOpBeds(sentOpBeds);
        for (int i = 0; i< numberSquaresHeight; i++){
            for (int j = 0; j< numberSquaresWidth; j++){
                Square mySquare = new Square(x, my_y, squaresize);
                Square opSquare = new Square(x,op_y,squaresize); //should find somewhere

                myBoard.add(mySquare);
                opBoard.add(opSquare);
                x+=squaresize;

            }
            x = 26+15;
            my_y+=squaresize;
            op_y+=squaresize;
        }



    }

    public boolean isSquareInBed(Square square, List<Bed> beds){
        for (Bed bed : beds){
            return bed.getBounds().contains(square.getBounds().x, square.getBounds().y);
        }
        return false;
    }

    public void setOpBoard(List<Square> opBoard) {
        this.opBoard = opBoard;
    }

    public void setMyBoard(List<Square> myBoard) {
        this.myBoard = myBoard;
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

    public void setMyBeds(List<Bed> beds){
        if (beds == null){
            setStartBeds();
        } else {
            myBeds = beds;
            //i tillegg endre på squaresene til å ha flowers
            for (Square mySquare : myBoard){
                if (isSquareInBed(mySquare,myBeds)){
                    mySquare.setHasFlower(true);
                }
            }

        }
    }
    private void setStartBeds(){
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

    /**
     * Moves Opponents bed to the opponent board as the sent beds have myboard coordinates
     * @param sentOpBeds
     * @return
     */

    public void setOpBeds(List<Bed> sentOpBeds){
        //TODO: find logic
        //should set OpBeds to be the beds that opponent has placed
        List<Bed> newBeds = new ArrayList<>();
        System.out.println("SentOpBeds: "+sentOpBeds);

        for (Bed bed : sentOpBeds){
            System.out.println("SentOpBed: "+bed.getPos_x()+","+bed.getPos_y());
            int size = bed.getSize();
            boolean horizontal = bed.isHorizontal();
            String texturePath = bed.getTexturePath();
            Bed newBed = new Bed(size, horizontal, texturePath);
            float y = bed.getPos_y()+distance;
            float x = bed.getPos_x();
            newBed.updatePosition(x, y);
            System.out.println("Newbead: "+newBed.getPos_x()+","+newBed.getPos_y());
            newBeds.add(newBed);
            //endre squares til å ha flower

        }

        //

        opBeds = newBeds;/*
        for (Square opSquare : opBoard){
            if (isSquareInBed(opSquare, opBeds)){
                opSquare.setHasFlower(true);
                System.out.println("Flower in square: "+opSquare.getBounds());
            }
        }*/

        for (Bed bed : opBeds){
            List<Square> squares = bed.getSquares(opBoard);
            System.out.println("Squareslist: " + squares);
            for (Square square : squares){
                square.setHasFlower(true);
                System.out.println("Square in opBed: "+square.getBounds());

            }
        }

    }


    /**
     * Updates status on won and gameOver. Checks if all beds of either opBeds or MyBeds is fully hit.
     * If so the game is over. If opBeds fully hit you won, if myBeds fully hit you lost.
     */
    private void updateStatus(){
        boolean myBedsFullyHit = true;
        boolean opBedsFullyHit = true;

        for (Bed bed : opBeds){
            if (!bed.isFullyHit(opBoard)){
                //If any of ops beds aren't fully hit, I have not won
                opBedsFullyHit = false;
            }
        }
        for (Bed bed : myBeds){
            if (!bed.isFullyHit(myBoard)){
                //If any of my beds aren't fully hit, op has not won
                myBedsFullyHit = false;
            }
        }
        if (myBedsFullyHit){
            won = false;
            gameOver = true;
        }
        else if (opBedsFullyHit){
            won = true;
            gameOver = true;
        }


    }

    /**
     * Returns if the player has won the game.
     */
    public boolean getWinner() {
        updateStatus();
        return won;
    }

    /**
     * Returns if the game is over.
     * @return
     */
    public boolean getGameOver(){
        updateStatus();
        return gameOver;
    }



}
