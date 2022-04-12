package com.mygdx.game.model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.HashSet;
import java.util.Set;

public class Player {

    //skal denne ha disse eller bare get metoder for det?
    private String username;
    private String password;
    private FireBaseInterface _FBIC;


    public Player(String username, String password){
        this._FBIC= FlowerPowerGame.getFBIC();
        //check username first
        _FBIC.newPlayer(username, password);
    }

    /**
     * gets username of player
     * @return username of current logged in player
     */
    public String getUsername() {
        return _FBIC.getUsername();
    }

    /**
     * checks if the username is valid and not already in use
     * @param username
     * @return
     */
    /*private boolean validUsername(String username){

    }*/




}
