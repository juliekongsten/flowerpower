package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.List;

public class Game {

    private int GID;
    private FireBaseInterface _FBIC;

    // TODO: må mer metoder til for å connecte med firebaseconnector

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

    public int getGID(){
        return this.GID;
    }

    public void setPlayerReady(){
        this._FBIC.setPlayerReady(this.GID);
    }

    public boolean getPlayersReady(){
        //Get opponents ready value from database
        return this._FBIC.getPlayersReady(this.GID);
    }

    public void storePlacedBeds(List<Bed> beds) {
        System.out.println("gets in to game");
        _FBIC.storeBeds(beds, GID);
    }

    public boolean isMyTurn(){
        boolean myTurn =this._FBIC.isMyTurn(this.GID);
        System.out.println("Game is my turn: "+myTurn);
        return myTurn;
    }

}
