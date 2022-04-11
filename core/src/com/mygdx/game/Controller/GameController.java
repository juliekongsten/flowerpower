package com.mygdx.game.Controller;

public class GameController {

    private String username;
    private String password;

    public void new_user(String username, String password){
        this.username = username;
        this.password=password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return password;
    }
}
