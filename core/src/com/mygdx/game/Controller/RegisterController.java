package com.mygdx.game.Controller;

import com.mygdx.game.model.Player;

public class RegisterController {

    private Player player;

    public RegisterController(String username, String password){
        player = new Player();
        player.registerPlayer(username, password);
        }

}
