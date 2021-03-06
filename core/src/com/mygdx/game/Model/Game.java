package com.mygdx.game.Model;

import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sun.security.util.ArrayUtil;

public class Game {

    private int GID;
    private FireBaseInterface _FBIC;
    private int distance;

    // TODO: må mer metoder til for å connecte med firebaseconnector

    /**
     * Constructor for joining existing game
     *
     * @param existingGID existing gamepin
     */

    public Game(int existingGID) {
        this.GID = existingGID;
        this._FBIC = FlowerPowerGame.getFBIC();
        this.distance = 359;
        List<Integer> gameIDs = _FBIC.getGameIDs();

        String i = Integer.toString(existingGID);
        System.out.print(gameIDs);
        System.out.println(existingGID);
        System.out.println(gameIDs.contains(i));

        if (gameIDs.contains(i)) {
            List<String> players = _FBIC.getPlayers(existingGID);
            if (players.size() >= 2) {
                throw new IllegalArgumentException("Too many players in game");
            } else if (players.contains(_FBIC.getUID())) {
                throw new IllegalArgumentException("Girly u already in");
            } else {
                _FBIC.joinGame(GID);
            }
        } else {
            throw new IllegalArgumentException("GameID does not exsist");
        }
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    /**
     * Constructor for creating new Game
     * Generates new gamepin
     */
    public Game() {
        this._FBIC = FlowerPowerGame.getFBIC();
        this.distance = 359;
        this.GID = generateGameID();
        System.out.println("THIS IS THE GID IN GAME CLASS: " + this.GID);
        _FBIC.createGame(GID);
    }

    /**
     * creates a new GameID that don't exist
     *
     * @return GID
     */
    private int generateGameID() {
        List<Integer> gameIDs = _FBIC.getGameIDs();
        this.GID = (int) ((Math.random() * (10000 - 1000)) + 1000);
        String i = Integer.toString(this.GID);
        if ((!gameIDs.contains(i))) {
            return GID;
        } else {
            return generateGameID();
        }
    }

    public int getDistance(){
        return this.distance;
    }

    /**
     * returns this games pin
     *
     * @return GID
     */
    public int getGID() {
        return this.GID;
    }

    /**
     * tells the database that the player is ready to start the game
     */
    public void setPlayerReady() {
        this._FBIC.setPlayerReady(this.GID);
    }

    public boolean getPlayersReady() {
        //Get opponents ready value from database
        List<Boolean> ready = this._FBIC.getPlayersReady(this.GID);
        System.out.println("Game getplayersready: " + ready);
        if (ready.contains(false) || ready.isEmpty()) {
            return false;
        } else if (!ready.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * tells the db to store the bed objects
     *
     * @param beds
     */
    public void storePlacedBeds(List<Bed> beds) {
        _FBIC.storeBeds(beds, GID);
    }
/*
    public ArrayList<Square> getMyMoves() {
        System.out.println("Getting opponents moves");
        ArrayList<Square> squareList = _FBIC.getMoves(GID);
        return squareList;
    }

    public ArrayList<Square> getOpMoves() {
        System.out.println("Getting opponents moves");
        ArrayList<Square> squareList = _FBIC.getOpMoves(GID);
        return squareList;
    }*/


    public void setMove(Square square) {
        _FBIC.setMove(square, this.getGID());
    }

    /**
     * if a player forfeits a game, the game should be deleted and the opponent should get notified
     */
    public void deleteGame() {
        //notify the other user too!
        _FBIC.leaveGame(GID);

    }

    public boolean hasForfeited() {
        _FBIC.OpHasForfeited(GID);
        boolean value = _FBIC.getOpHasForfeited();
        if(value){
            _FBIC.updateScore();
        }
        return value;
    }

    public void excited() {
        _FBIC.forfeitedGame(GID);
    }


    public Map<String, Object> retrievePlacedBeds() {
        return _FBIC.retrieveBeds(GID);
    }

    public boolean isMyTurn() {
        boolean myTurn = false;
        if (getPlayersReady()) {
            myTurn = this._FBIC.isMyTurn(this.GID);
        }
        return myTurn;
    }

    public void setTurnToOtherPlayer() {
        System.out.println("setTurnToOtherPlayer in game");
        this._FBIC.setTurnToOtherPlayer(this.GID);
    }

    public boolean checkForGameStart() {
        List<String> players = _FBIC.getPlayers(this.GID); //returnerer 2 selvom det kun er 1 i db

        if (players.size() == 0) {
            return false;
        } else if (players.size() == 1) {
            return false;
        } else if (players.size() == 2) {
            return true;
        }
        return false;
    }
    public void updateScore(){
        _FBIC.updateScore();
        //System.out.println("Dette er scoren i player: " + this.score);
    }

    public void clearPlayers() {
        _FBIC.clearPlayers();
    }

    //henter vi siste moves til den andre spilleren
    public Square getHit() {
        Map<String, Object> hit;
        hit = _FBIC.getHit(this.GID);
        if(hit!=null) {
            int x = 0;
            int y = 0;
            int size = 0;
            boolean flower = false;
            System.out.println("DETTE ER MOVESENE VERDI: " + hit);
            String formatString = hit.toString().replace("{", "").replace("}", "");
            String[] values = formatString.split(",");
            for (int i = 0; i < values.length; i++) {
                System.out.println("MOVSENE verdien en og en: " + values[i]);
                if (values[i].contains("pos_x")) {
                    String[] pos_x = values[i].split("=");
                    x = Integer.parseInt(pos_x[1]);
                }
                if (values[i].contains("pos_y")) {
                    String[] pos_y = values[i].split("=");
                    y = Integer.parseInt(pos_y[1]);
                    y -= distance; //nå lik koordinatet på vårt eget board
                }
                if (values[i].contains("Size")) {
                    String[] sizeString = values[i].split("=");
                    size = Integer.parseInt(sizeString[1]);
                }
                if (values[i].contains("Flower")) {
                    String[] sizeString = values[i].split("=");
                    flower = Boolean.parseBoolean(sizeString[1]);
                }
            }

            Square square = new Square(x, y, size);
            square.setHasFlower(flower);
            square.setHit(true);
            return square;
        }
        return null; //vet den er i et av mine bed
    }
}


