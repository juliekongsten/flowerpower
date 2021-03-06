package com.mygdx.game.Model;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

public class Player {

    private String username;
    private String UID;
    private int score;
    private FireBaseInterface _FBIC;

    /**
     * Creates a new player for the game
     */

    //oppretter nå en ny spiller i konstruktøren
    //TODO: what are we going to put here thooo
    public Player() {

    }

    /**
     * Method used by RegisterController to register a new user.
     * Throws exception if something goes wrong, the exception is handled by the view
     *
     * @param username
     * @param password
     * @throws Exception
     */
    public void registerPlayer(String username, String password) throws Exception {
        this._FBIC = FlowerPowerGame.getFBIC();
        //check username first
        _FBIC.newPlayer(username, password);
        boolean isDone = this._FBIC.getIsDone();
        /*Waiting for newplayer-task in firebaseconnector to be completed as we
        are interested in the outcome there before moving on
         */
        while (!isDone) {
            isDone = this._FBIC.getIsDone();
        }

        if (_FBIC.getException() != null) {
            System.out.println("Could not create user, wrong input");
            throw _FBIC.getException();
        } else {
            System.out.println("Could create user with username " + _FBIC.getUsername());
            this.username = _FBIC.getUsername();
            this.UID = _FBIC.getUID();
            // Creates new player therefore the score i 0
            this.score = 0;
        }

    }



    /**
     * Method to sign an existing player
     *
     * @param username
     * @param password
     */
    public void signIn(String username, String password) throws Exception {
        this._FBIC = FlowerPowerGame.getFBIC();
        _FBIC.signIn(username, password);
        boolean ready = this._FBIC.getIsDone();
        while (!ready) {
            ready = this._FBIC.getIsDone();
        }
        if (_FBIC.getException() != null) {
            throw _FBIC.getException();
        }


    }


    public void signOut() {
        this._FBIC = FlowerPowerGame.getFBIC();
        _FBIC.signOut();
    }

    /**
     * gets username of player
     *
     * @return username of current logged in player
     */
    // TODO: kunne ikke denne vært den lokale variabelen vi har så slipper vi kall på db
    public String getUsername() {
        return _FBIC.getUsername();
    }



    public Integer getScore() {
       // return _FBIC.getScore(this.UID);
        return this.score;
    }


    /**
     * checks if the username is valid and not already in use
     * @param username
     * @return
     */
    /*private boolean validUsername(String username){}
     */

}