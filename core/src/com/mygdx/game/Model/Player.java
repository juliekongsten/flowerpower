package com.mygdx.game.model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

public class Player {

    private String username;
    private String UID;
    private FireBaseInterface _FBIC;


    //oppretter nå en ny spiller i konstruktøren
    //TODO: what are we going to put here thooo
    public Player(){

    }


    public void registerPlayer(String username, String password){
        this._FBIC= FlowerPowerGame.getFBIC();
        //check username first
        _FBIC.newPlayer(username, password);
        this.username = _FBIC.getUsername();
        this.UID = _FBIC.getUID();
        //_FBIC.writeUserDataToDb(this);
    }





    //TODO: boolean? error handling in firebaseconnector?
    public void signIn(String username, String password){
        _FBIC.signIn(username, password);
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
