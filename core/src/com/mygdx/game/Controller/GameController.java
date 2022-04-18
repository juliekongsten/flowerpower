package com.mygdx.game.Controller;
import com.mygdx.game.Model.Player;
import com.mygdx.game.Model.Game;

public class GameController {
    private Game game;
    private int GID;

    public GameController(){
        //default constructor update later

    }
    public void joinGame(int GID){
        Game game = new Game(GID);
        this.game = game;
    }

    public void createGame(){
        Game game = new Game();
        this.game = game;
        this.GID = game.getGID();
    }

    public int getGID(){
        return this.GID;
    }


}
