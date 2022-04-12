package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.HashSet;
import java.util.Set;

public class Player {

    private String username;
    private String password;
    private FireBaseInterface _FBIC;


    public Player(String username, String password){
        // sende denne videre til db for å sjekke
        /*
        New player created, must check with database if username exists

         */
        this.username = username;
        this.password = password;
        this._FBIC= FlowerPowerGame.getFBIC();


    }

    public void notifyDB(){

    }


    public void check_valid(String playerUsername, String playerPassword){


    }

    public String getUsername(){
        return this.username;
    }


    //TODO: burde vi ha med denne eller ikke? dårlig praksis å dele passord?

    public String getPassword() {
        return password;
    }
}
