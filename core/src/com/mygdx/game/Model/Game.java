package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Player;

public class Game {

    private Player player1;
    private Player player2;
    private int GID;
    private FireBaseInterface _FBIC;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    public Game(String username) {
        //this.player1 = player1;
        this._FBIC= FlowerPowerGame.getFBIC();
        this.GID = (int) ((Math.random() * (10000 - 1000)) + 1000);
    }
    public int getGID(){
        return this.GID;
    }

    public void joinGame(Player player2){
        this.player2 = player2;
    }


}
