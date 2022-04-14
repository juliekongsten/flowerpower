package com.mygdx.game;


import com.mygdx.game.Model.Player;

/**
 * Currently not used but required for communication between modules
 */
public class fireBaseInterfaceCore implements FireBaseInterface{
    @Override
    public void writeToDb(String target, String value) { }

    @Override
    public void readFromDb() {

    }

    @Override
    public void newPlayer(String username, String password) {

    }

    @Override
    public void signIn(String username, String password) {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getUID() {
        return null;
    }

    @Override
    public void createGame(){}

    @Override
    public void joinGame(int GID){}

    /**@Override
    public void writeUserDataToDb(Player player) {

    }**/


}
