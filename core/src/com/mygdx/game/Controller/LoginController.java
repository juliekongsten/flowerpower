package com.mygdx.game.Controller;

import com.mygdx.game.Model.Player;

public class LoginController {

    private Player player;
    private Exception exception;

    /**
     *
     * @param username mail for the user
     * @param password password for the user
     * Controller that handles the log in for the user
     */
    public LoginController(String username, String password){
        player = new Player();
        player.signIn(username, password);
    }

    public boolean checkValid(){
        if(player.getException()!=null){
            this.exception = player.getException();
            return false;
        }
        return true;
    }
    public Exception getException(){
        return this.exception;
    }

}
