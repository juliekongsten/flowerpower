package com.mygdx.game;


import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Player;
import com.mygdx.game.Model.Square;

import java.util.ArrayList;

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
    public List<Boolean> getPlayersReady(int GID) {
        return null;
    }


    @Override
    public boolean isMyTurn(int gameID) {
        return false;
    }


    @Override
    public void createGame(int GID){}

    @Override
    public void joinGame(int GID){}

    @Override
    public void storeBeds(List<Bed> beds, int GID) {}

    @Override
    public Map<String, Object> retrieveBeds(int GID) { return null;}

    @Override
    public void leaveGame(int gid) {

    }

    @Override
    public void forfeitedGame(int gid) {}


    @Override
    public void setMove(Square square, int GID) {

    }

    @Override
    public boolean getOpHasForfeited() {
        return false;
    }

    @Override
    public void OpHasForfeited(int gamePin){

    }

    @Override
    public void clearPlayers() {

    }

    @Override
    public Map<String, Object> getHit(int GID){return null;}

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

    @Override
    public void setTurnToOtherPlayer(int GID){}
    @Override
    public int getScore(String UID) {
        return 0;
    }

    @Override
    public ArrayList<String> getUserIDs() {
        return null;
    }

    @Override
    public String getNameFromUID(String UID) {
        return null;
    }

    @Override
    public HashMap<Integer, String> getScoreMap() {
        return null;
    }
    @Override
    public void updateScore(){}



}
