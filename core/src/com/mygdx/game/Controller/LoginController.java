package com.mygdx.game.Controller;

import com.mygdx.game.Model.Player;

public class LoginController {

    private Player player;

    public LoginController(String username, String password){
        player = new Player();
        player.signIn(username, password);
    }

}
