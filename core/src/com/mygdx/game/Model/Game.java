package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Player;

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
        _FBIC.joinGame(GID);
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    /**
     * Constructor for creating new Game
     * Generates new gamepin
     */
    public Game() {
        //this.player1 = player1;
        this._FBIC= FlowerPowerGame.getFBIC();
        this.GID = (int) ((Math.random() * (10000 - 1000)) + 1000);
        System.out.println("THIS IS THE GID IN GAME CLASS: " + this.GID);
        _FBIC.createGame(GID);

    }
    public int getGID(){
        return this.GID;
    }

    public void setPlayerReady(){
        this._FBIC.setPlayerReady(this.GID);
    }

    //TODO: fjerne denne, tror ikke vi trenger den mtp konstruktøren gjør det samme
    public void joinGame(int existingGID){
        this.GID= existingGID;
    }


}
