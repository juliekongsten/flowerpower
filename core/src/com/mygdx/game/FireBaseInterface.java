package com.mygdx.game;

import com.mygdx.game.Model.Player;

/**
 * Interface to enable communication between modules
 * Contains methods implemented by fireBaseConnector to access db
 */
public interface FireBaseInterface {
    public void writeToDb(String target, String value);
    public void readFromDb();
    void newPlayer(String username, String password);
    void signIn(String username, String password);
    String getUsername();

    String getUID();

    Exception getException();

    boolean getIsDone();
    void setPlayerReady(int GID);

    //void writeUserDataToDb(Player player);
    void createGame(int GID);
    void joinGame(int GID);

}
