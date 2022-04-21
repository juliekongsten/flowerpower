package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Player;

import java.util.List;

public class Game {

    private Player player1;
    private Player player2;
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
        boolean isDone = this._FBIC.getIsDone();
        System.out.println(isDone);
        /*Waiting for newplayer-task in firebaseconnector to be completed as we
        are interested in the outcome there before moving on
         */
        while (!isDone){
            isDone = this._FBIC.getIsDone();
        }
        String i = Integer.toString(existingGID);
        System.out.print(gameIDs);
        System.out.println(existingGID);
        System.out.println(gameIDs.contains(i));
        boolean existing = false;
        /*for (Integer id : gameIDs){
            if (id==existingGID){
                List<String> players =_FBIC.getPlayers(existingGID);
                existing = true;
                if (players.size()>2){
                    throw new IllegalArgumentException("Too many players in game");
                }
                else{
                    _FBIC.joinGame(GID);
                }

            }

        }
        if (!existing){
            throw new IllegalArgumentException("GameID does not exsist");
        }*/
        if (gameIDs.contains(i)){
            List<String> players =_FBIC.getPlayers(existingGID);
            if (players.size()>2){
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
        //this.player1 = player1;
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
        if ((!gameIDs.contains(GID))){
            return GID;
        }
        else{
            return generateGameID();
        }
    }

    public int getGID(){
        return this.GID;
    }

    //TODO: fjerne denne, tror ikke vi trenger den mtp konstruktøren gjør det samme
    public void joinGame(int existingGID){
        this.GID= existingGID;
    }


}
