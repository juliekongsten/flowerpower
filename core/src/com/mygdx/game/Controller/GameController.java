package com.mygdx.game.Controller;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;
import com.mygdx.game.Model.HighScoreList;
import com.mygdx.game.Model.Player;
import com.mygdx.game.Model.Game;

import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Square;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private Game game;
    private int GID;
    private boolean gameStarted;
    private Player player;
    private Button button;

    //har per nå laget kun en liste, kan heller ha liste i liste for å lettere navigere seg opp/ned/sidelengs men har ikke det nå
    private List<Square> opBoard = new ArrayList<>();
    private List<Square> myBoard = new ArrayList<>();
    private int squaresize;
    private int numberSquaresHeight;
    private int numberSquaresWidth;
    private int numberBeds = 5;
    private List<Bed> myBeds = new ArrayList<>();
    private List<Bed> opBeds = new ArrayList<>();

    private int distance;

    private boolean gameOver = false;
    private boolean won = false;
    private Square hitSquare;
    private HighScoreList highScoreList;


    public GameController(){
        //tenker her at vi kan ha satt tall for de forskjellige vanskelighetsgradene
        //burde endres uansett om vi implementerer vanskelighetsgrader eller ikke, for å vise modifiability
        squaresize = 32;
        numberSquaresHeight = 6;
        numberSquaresWidth = 9;
        gameStarted = false;



        setStartBoards();
        setMyBeds(null);

    }




    public void joinGame(int GID) {

        Game game = new Game(GID);
        this.game = game;
        this.distance = game.getDistance();
        gameStarted = true;

    }


    public void createGame(){
        Game game = new Game();
        this.game = game;
        this.GID = game.getGID();
        this.distance = game.getDistance();
        gameStarted = true;
    }



    public int getGID(){
        return this.GID;
    }

    public boolean isSquareInBed(Square square, List<Bed> beds){
        boolean isInBed = false;
        for (Bed bed : beds){
            if (bed.getBounds().contains(square.getBounds().x+2, square.getBounds().y+2)){
                isInBed = true;
                break;
            }
        }
        return isInBed;
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
        //TODO: find logic to update opponent as well
        if (!opBoard.contains(square)){
            return false;
        }
        game.setMove(square);
        // isSquareInBed(square, opBeds);
        square.setHit(true);
        return square.hasFlower();

    }


    public void setMyBeds(List<Bed> beds){
        if (beds == null){
            setStartBeds();
        } else {
            myBeds = beds;
            /*if (gameStarted) {
                game.storePlacedBeds(myBeds);
            }*/
            game.storePlacedBeds(myBeds);
            for (Square mySquare : myBoard){
                mySquare.setHasFlower(false); //nullstille tilfelle det var igjen fra tidligere
                if (isSquareInBed(mySquare,myBeds)){
                    mySquare.setHasFlower(true);
                }
            }
        }
    }

    private void setStartBeds(){
        //TODO: (Low Priority) Have beds as argument so we can have different beds for different games
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

    private void setStartBoards(){
        //TODO: (Low priority) Get x- and y-values without hardkoding :D
        //må hente x og y-verdier fra view heller sånn at vi får riktige :D
        //henter nå fra printsetting i gameview, er nok lurt å gjøre det mindre hardkoding
        int x = 26+15; //201
        int my_y = 65+12;
        int op_y = 424+12; //596
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

    public void clear(){
        this.opBeds = new ArrayList<>();
        setStartBoards();
        setMyBeds(null);

    }

    /**
     * Set my ready to true in the database
     */
    public void setPlayerReady(){
        this.game.setPlayerReady();
    }

    public boolean getPlayersReady(){
        boolean ready = this.game.getPlayersReady();
        System.out.println("controller playersready: "+ready);

        return ready;
    }

    /**
     * Receive opponents beds from database
     */
    public void receiveOpBeds(){
        //TODO: Logic, database
        List<Object> bedsList = new ArrayList<>();
        List<Bed> result = new ArrayList<>();
        Map<String, Object> receivedOpBeds;
        receivedOpBeds = game.retrievePlacedBeds();
        bedsList.addAll(receivedOpBeds.values());
        for (int j=0; j<bedsList.size(); j++) {
            String newString = bedsList.get(j).toString();
            String pos_yString;
            String pos_xString;
            String horizontalString;
            String sizeString;
            String texturePath;
            String[] parts = newString.split(", ");
            List<String> valueList = new ArrayList<>();
            for (String part : parts) {
                valueList.add(part.split("=")[1]);
            }
            pos_yString = valueList.get(0);
            horizontalString = valueList.get(1);
            pos_xString = valueList.get(2);
            sizeString = valueList.get(3);
            String texturePathString = valueList.get(4);
            String substring = texturePathString.substring(0, texturePathString.length()-1);
            texturePath = substring;
            Bed bed = new Bed(Integer.parseInt(sizeString), Boolean.parseBoolean(horizontalString), texturePath);
            bed.updatePosition(Float.parseFloat(pos_xString), Float.parseFloat(pos_yString));
            result.add(bed);
        }
        moveOpBeds(result);
    }

    /**
     * Sends my beds to database
     * @param beds
     */
    public void sendMyBeds(List<Bed> beds){

    }

    /*public ArrayList<Square> getMyMoves(){
        return this.game.getMyMoves();
    }
    public ArrayList<Square> getOpMoves(){
        return this.game.getOpMoves();
    }*/



    /**
     * Moves Opponents bed to the opponent board as the sent beds have myboard coordinates
     * @param receivedOpBeds
     * @return
     */
    private void moveOpBeds(List<Bed> receivedOpBeds){
        List<Bed> newBeds = new ArrayList<>();

        for (Bed bed : receivedOpBeds){
            int size = bed.getSize();
            boolean horizontal = bed.isHorizontal();
            String texturePath = bed.getTexturePath();
            Bed newBed = new Bed(size, horizontal, texturePath);
            float y = bed.getPos_y()+distance;
            float x = bed.getPos_x();
            newBed.updatePosition(x, y);
            newBeds.add(newBed);

        }
        opBeds = newBeds;

        for (Bed bed : opBeds){
            List<Square> squares = bed.getSquares(opBoard);
            for (Square square : squares){
                square.setHasFlower(true);

            }
        }
        System.out.println("MOVED BEDS: " + newBeds);
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
            game.updateScore();
            gameOver = true;
        }
    }
    public HashMap<String, Integer> getHighScore(){
        this.highScoreList = new HighScoreList();
        HashMap<String, Integer> topFive = highScoreList.getTopFive();
        return topFive;

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

    public void myForfeited() {
        game.excited();
    }

    /**
     * Returns if the opponent has forfeited, (pressed on "go back to menu") in GameView
     * @return
     */
    public boolean getOpForfeited(){
       return game.hasForfeited();
    }


    public void deleteGame() {
        System.out.println("deleting");
        game.clearPlayers();
        game.deleteGame();
    }

    public void clearPlayers(){
        game.clearPlayers();
    }


    public boolean isMyTurn(){
        boolean myTurn = this.game.isMyTurn();
        System.out.println("Controller is my turn: "+myTurn);
        return myTurn;
    }

    public void setTurnToOtherPlayer(){
        System.out.println("setTurnToOtherPlayer in controller");
        //this.hitSquare = this.game.getHit();
        //null første gang du bytter  - du startyet
        this.game.setTurnToOtherPlayer();
    }

    public boolean checkForGameStart() {
        boolean start = this.game.checkForGameStart();
        System.out.println("start status: "+start);
        return start;

    }

    public Square getHitSquare(){
        //ikke eksisterer - den eksisterer ikke
        this.hitSquare = this.game.getHit();
        return this.hitSquare;

    }





}
