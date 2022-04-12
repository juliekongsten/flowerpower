package com.mygdx.game.desktop;

import com.mygdx.game.FireBaseInterface;

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


}
