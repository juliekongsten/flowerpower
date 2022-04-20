package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.ArrayList;

public class Player {

    private String username;
    private String UID;
    private FireBaseInterface _FBIC;
    private Exception exception = null;

    /**
     * Creates a new player for the game
     */

    //oppretter nå en ny spiller i konstruktøren
    //TODO: what are we going to put here thooo
    public Player(){

    }


    public void registerPlayer(String username, String password){
        this._FBIC= FlowerPowerGame.getFBIC();
        //check username first
        // if(passwordCheck(password)&&usernameCheck(username))
        _FBIC.newPlayer(username, password);
        this.username = _FBIC.getUsername();
        this.UID = _FBIC.getUID();
        System.out.println("Could not create user, wrong input");
            //TODO: her må hele systemet stoppe og ikke gå videre!



        //_FBIC.writeUserDataToDb(this);
    }






    public void signIn(String username, String password){
        this._FBIC= FlowerPowerGame.getFBIC();
        _FBIC.signIn(username, password);
        if (_FBIC.getExecption()!=null){
            // TODO: denne må sendes tilbake til bruker
            this.exception = _FBIC.getExecption();
            System.out.println(exception);
        }


    }

    public Exception getException(){
        return this.exception;
    }

    /**
     * Check for password
     * @param password given
     * @return boolean value
     */

    public boolean passwordCheck(String password){
        if (password.length()<6){
            System.out.println("Password too short!");
            return false;
        }
        return true;
    }

    /**
     * Checkf ro username must be email
     * @param username written
     * @return boolean value
     */

    public boolean usernameCheck(String username){
        if(!username.contains("@")){
            System.out.println("Username must contain @!");
            return false;
        }
        if(!username.contains(".com") || !username.contains(".no")){
            System.out.println("Username must contain .no or .com");
            return false;
        }
        return true;
    }


    /**
     * gets username of player
     * @return username of current logged in player
     */
    // TODO: kunne ikke denne vært den lokale variabelen vi har så slipper vi kall på db
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
