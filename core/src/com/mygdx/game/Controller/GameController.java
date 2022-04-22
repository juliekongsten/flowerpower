package com.mygdx.game.Controller;
import com.mygdx.game.Model.Player;
import com.mygdx.game.Model.Game;

import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Square;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Game game;
    private int GID;
    private boolean gameStarted;
    private Player player;

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
        gameStarted = true;

    }

    public void createGame(){
        Game game = new Game();
        this.game = game;
        this.GID = game.getGID();
        gameStarted = true;
    }



    public int getGID(){
        return this.GID;
    }

    public boolean isSquareInBed(Square square, List<Bed> beds){
        for (Bed bed : beds){
            return bed.getBounds().contains(square.getBounds().x, square.getBounds().y);
        }
        return false;
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
        square.setHit(true);
        return square.hasFlower();

    }

    public void setMyBeds(List<Bed> beds){
        System.out.println("setmybeds");
        if (beds == null){
            setStartBeds();
        } else {
            myBeds = beds;
            if (gameStarted) {
                System.out.println("gets in game controller");
                game.storePlacedBeds(myBeds);
            }
            for (Square mySquare : myBoard){
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
        int x = 26+15;
        int my_y = 65+12;
        int op_y = 424+12;
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
        List<Bed> receivedOpBeds = myBeds; //get from database
        moveOpBeds(receivedOpBeds);
    }

    /**
     * Sends my beds to database
     * @param beds
     */
    public void sendMyBeds(List<Bed> beds){
        //TODO: Logic
    }

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

    /**
     * Returns if the opponent has exited, (pressed on "go back to menu") in placebedsview,
     * before the game has started
     * @return
     */
    public boolean getOpExited() {
        //TODO get this information from DB
        return false;
    }

    /**
     * Returns if the opponent has forfeitet, (pressed on "go back to menu") in GameView
     * @return
     */
    public boolean getOpForfeitet() {
        //TODO get this information from DB
        return false;
    }

    public boolean isMyTurn(){
        boolean myTurn = this.game.isMyTurn();
        System.out.println("Controller is my turn: "+myTurn);
        return myTurn;
    }

    public void setTurnToOtherPlayer(){
        this.game.setTurnToOtherPlayer();
    }

    public boolean checkForGameStart() {
        boolean start = this.game.checkForGameStart();
        System.out.println("start status: "+start);
        return start;

    }
}
