package com.mygdx.game;


import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Player;
import java.util.Map;

import java.util.HashMap;
import java.util.List;

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
    public Exception getException() { return null; }

    @Override
    public boolean getIsDone() {
        return false;
    }
    @Override
    public void setPlayerReady(int GID){}


    @Override
    public void createGame(int GID){}

    @Override
    public void joinGame(int GID){}

    @Override
    public void storeBeds(List<Bed> beds, int GID) {}

    @Override
    public Map<String, Object> retrieveBeds(int GID) { return null;}

    @Override
    public List<String> getPlayers(int gameID){
        return null;
    }

    @Override
    public List<Integer> getGameIDs(){
        return null;
    }

    @Override
    public void signOut() {}

    /**@Override
    public void writeUserDataToDb(Player player) {

    }**/


}
