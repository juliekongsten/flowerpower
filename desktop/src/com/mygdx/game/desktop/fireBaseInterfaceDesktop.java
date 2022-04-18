package com.mygdx.game.desktop;

import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.Model.Player;

/**
 * Currently not used but required for communication between modules
 */
public class fireBaseInterfaceDesktop implements FireBaseInterface {

    @Override
    public void writeToDb(String target, String value) {}

    @Override
    public void readFromDb() { }

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


    /*@Override
    public void writeUserDataToDb(Player player) {}*/

    @Override
    public void createGame(int GID){}

    @Override
    public void joinGame(int GID){}




}
