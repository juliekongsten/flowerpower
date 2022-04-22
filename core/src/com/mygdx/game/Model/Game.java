package com.mygdx.game.Model;

import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int GID;
    private FireBaseInterface _FBIC;

    /**
     *  Constructor for joining existing game
     * @param existingGID existing gamepin
     */
    public Game(int existingGID) {
        this.GID= existingGID;
        this._FBIC= FlowerPowerGame.getFBIC();
        List<Integer> gameIDs = _FBIC.getGameIDs();

        String i = Integer.toString(existingGID);
        System.out.print(gameIDs);
        System.out.println(existingGID);
        System.out.println(gameIDs.contains(i));

        if (gameIDs.contains(i)){
            List<String> players =_FBIC.getPlayers(existingGID);
            if (players.size()>=2){
                throw new IllegalArgumentException("Too many players in game");
            }
            else if (players.contains(_FBIC.getUID())){
                throw new IllegalArgumentException("Girly u already in");
            }
            else{
                _FBIC.joinGame(GID);
            }
        }
        else{
            throw new IllegalArgumentException("GameID does not exsist");
        }
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    /**
     * Constructor for creating new Game
     * Generates new gamepin
     */
    public Game() {
        this._FBIC= FlowerPowerGame.getFBIC();
        this.GID = generateGameID();
        System.out.println("THIS IS THE GID IN GAME CLASS: " + this.GID);
        _FBIC.createGame(GID);
    }

    /**
     * creates a new GameID that don't exist
     * @return GID
     */
    private int generateGameID(){
        List<Integer> gameIDs = _FBIC.getGameIDs();
        this.GID = (int) ((Math.random() * (10000 - 1000)) + 1000);
        String i = Integer.toString(this.GID);
        if ((!gameIDs.contains(i))){
            return GID;
        }
        else{
            return generateGameID();
        }
    }

    /**
     * returns this games pin
     * @return GID
     */
    public int getGID(){
        return this.GID;
    }

    /**
     * tells the database that the player is ready to start the game
     */
    public void setPlayerReady(){
        this._FBIC.setPlayerReady(this.GID);
    }

    //TODO: fjerne denne, tror ikke vi trenger den mtp konstruktøren gjør det samme
    public void joinGame(int existingGID){
        this.GID= existingGID;
    }

    /**
     * tells the db to store the bed objects
     * @param beds
     */
    public void storePlacedBeds(List<Bed> beds) {
        _FBIC.storeBeds(beds, GID);
    }

    /**
     * if a player forfeits a game, the game should be deleted and the opponent should get notified
     */
    public void deleteGame(){
        //notify the other user too!
        _FBIC.leaveGame(GID);

    }

}
