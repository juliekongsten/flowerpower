package com.mygdx.game.Controller;
import com.mygdx.game.Model.Player;

public class GameController {

    private String username;
    private String password;

    public void newPlayer(String username, String password){
        Player player = new Player(username, password);
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return password;
    }
}
