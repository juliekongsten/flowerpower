package com.mygdx.game.Controller;

import com.mygdx.game.Model.Player;

public class RegisterController {

    private Player player;

    /**
     * @param username mail for the user
     * @param password password for the user
     * Controller that handles registering new user, throws exception if something goes wrong
     */
    public RegisterController(String username, String password) throws Exception {
        player = new Player();
        player.registerPlayer(username, password);
        }

}
