package com.mygdx.game.Controller;

import com.mygdx.game.Model.Player;

public class PlayerController {
    private Player player;

    public PlayerController(){
        //konstruktør, gjør noe her?
    }


    public void logIn(String username, String password) throws Exception {
        player = new Player();
        player.signIn(username, password);

    }

    public void register(String username, String password) throws Exception {
        player = new Player();
        player.registerPlayer(username, password);
    }

    public void logOut(){
        System.out.println("Kommer til logg ut playercontroller");
        //player = new Player();
        player.signOut();
    }
}
