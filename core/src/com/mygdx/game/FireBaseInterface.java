package com.mygdx.game;

import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Player;
import com.mygdx.game.Model.Square;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    void signOut();

    Exception getException();

    boolean getIsDone();
    void setPlayerReady(int GID);
    List<Boolean> getPlayersReady(int GID);

    boolean isMyTurn(int gameID);

    List<String> getPlayers(int GID);
    List<Integer> getGameIDs();

    void setTurnToOtherPlayer(int GID);

    //void writeUserDataToDb(Player player);
    void createGame(int GID);
    void joinGame(int GID);
    void storeBeds(List<Bed> beds, int GID);
    void setMove(Square square, int GID);
    ArrayList<Square> getMoves(int GID);
    ArrayList<Square> getOpMoves(int GID);
    Map<String, Object> retrieveBeds(int GID);

    void leaveGame(int gid);

    void forfeitedGame(int gid);

    boolean getOpHasForfeited();

    void OpHasForfeited(int gid);

    void clearPlayers();
}
