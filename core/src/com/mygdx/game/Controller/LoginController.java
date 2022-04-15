package com.mygdx.game.Controller;

import com.mygdx.game.Model.Player;

public class LoginController {

    private Player player;

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

}
