package com.mygdx.game.Controller;

public class RegisterController {

    private com.mygdx.game.model.Player player;
    private String username;
    private String password;

    public RegisterController(String username, String password){
        player = new com.mygdx.game.model.Player(username, password);
    }

    //TODO: change these? do we need them? want to get from player instead
    public String getUsername(){
        return this.username;
    }

    public String getPassword() {
        return password;
    }

}
