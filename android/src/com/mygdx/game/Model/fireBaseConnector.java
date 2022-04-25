package com.mygdx.game.Model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.mygdx.game.FireBaseInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fireBaseConnector connects the application to the Firebase Realtime Database and implements the
 * methods describes in FireBaseInterface from the core module.
 * This class handles all database activity
 */

public class fireBaseConnector implements FireBaseInterface {
     private final FirebaseDatabase database;
     private DatabaseReference myRef;
     private final FirebaseAuth mAuth;
     private Exception exception = null;
     private boolean isDone = false;
     private String playerTurn;
     private List<String> players = new ArrayList<>();
     private List<Integer> gameIDs;
    private ArrayList<String> userIDs;
     private static int moveCount = 1;
     private int opMoveCount = 1;
     private Map<String, Object> beds;
     private Map<String, Object> hit;
     private boolean playersReady = true;
     private String UID;
     private List<Boolean> playersReadyList;
    private int score;
    private int highScore;
    private String nameFromUID;
    private  HashMap< Integer, String> scoreMap;


    /**
     * Constructor that gets an instance of the database and authorization
     */
    public fireBaseConnector() {
        database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app");
        mAuth = FirebaseAuth.getInstance();
        //TODO: Find out if this is okay, and just use this.UID instead of getUID later

    }


    /**
     *writeToDb writes to the database
     * @param target where you want to change the value
     * @param value new value
     */
    @Override
    public void writeToDb(String target, String value){
        myRef = database.getReference(target);
        myRef.setValue(value);

    }


    /**
     * readFromDb creates a listener that listens to the location of myRef and logs the value on that reference
     * will implement the publish subscriber pattern
     */
    @Override
    public void readFromDb() {
        /**
         * addValueEventListener(new ValueEventListener()) is used to receive events about data changes at a location
         */
        myRef.addValueEventListener(new ValueEventListener() {
            /**
             * This method will be called with a snapshot of the data at this location.
             * It will also be called each time that data changes.
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server,
             * or is removed as a result of the security and Firebase Database rules.
             * @param error
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * This method adds a user to the database with email and password authentication
     * @param username mail for the user
     * @param password password for the user
     */
    public void newPlayer(String username, String password) {
        this.exception = null;
        isDone = false;

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, the user is now both registered and signed in
                Log.d(TAG, "createUserWithEmail:success");
                //TODO: maybe send the user back to the player class to get id? otherwise remove
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference usersRef = database.getReference().child("users").child(user.getUid());
                //TODO: put in whole object simultaneously
                // Her kan mer data puttes når vi ønsker å lagre highscore osv.
                Map userData = new HashMap<>();
                userData.put("Mail", user.getEmail());
                usersRef.setValue(userData);
                userData.put("Score", 0);
                usersRef.setValue(userData);

            }
            else if (task.getException() instanceof FirebaseAuthUserCollisionException)
            {
                //If email already registered.
                this.exception = new CustomException("Email already in use");

            }else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                //if password not 'stronger'
                this.exception = new CustomException("Weak password");
            }
            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                //If email are in incorret  format
                this.exception = new CustomException("Invalid email");

            }
            else {
                // Sign in failed
                //TODO: send message back to register class for error handling
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                this.exception = new CustomException("Unknown exception");

            }
            isDone = true;

        });

    }

    /**
     * method that signs in an existing user
     * @param username
     * @param password
     */
    public void signIn(String username, String password){
        this.exception = null;
        this.isDone = false;
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success,
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, user.getEmail());

                    }
                    else if (task.getException() instanceof FirebaseAuthInvalidUserException){
                        this.exception = new CustomException("Invalid email/password");
                    }
                    else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        //Thrown when one or more of the credentials passed to a method fail to
                        // identify and/or authenticate the user subject of that operation.
                        //--> email OR password wrong
                        this.exception = new CustomException("Invalid email/password");

                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        this.exception = new CustomException("Unknown exception");

                    }
                    isDone = true;
                });

    }


    /**
     * returns current users username
     * @return
     */
    public String getUsername(){
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getEmail();
    }

    public String getUID(){
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getUid();
    }
    public Exception getException(){
        return this.exception;
    }
    /**
     * method that sign out current user
     */

    public void signOut(){
        try{
            mAuth.signOut();
            Log.d(TAG, "signOut:success");
        }catch (Exception e){
            //TODO: fix exception
            e.printStackTrace();

        }

    }

    /**
     * Method that updates the score for the user
     * Increments the score
     */

    public void updateScore(){
        DatabaseReference usersRef = database.getReference().child("users").child(this.getUID());
        DatabaseReference scoreRef = usersRef.child("/Score");
        int currentScore = this.getScore(this.UID);
        scoreRef.setValue(currentScore+1);
    }

    /**
     * Gets the score for the current user
     * @return score for the user
     */

    public int getScore(String userID){
        isDone = false;
        score = 0;
        DatabaseReference usersRef = database.getReference().child("users").child(userID);
        DatabaseReference scoreRef = usersRef.child("/Score");
        scoreRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    isDone = true;
                } else {
                    Object userScore = task.getResult().getValue();
                    if(userScore==null){
                        score = 0; // User doesn't have score attribute yet
                        isDone=true;
                    }else{
                        score = Integer.parseInt(userScore.toString());
                        System.out.println("Dette er final score i else : " + score);
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        isDone = true;
                    }
                    isDone=true;
                }
            }
        });
        while(!isDone) {
            //System.out.println("Getting the score! ");
            System.out.println("Dette er score i while : " + this.score);
        }
        System.out.println("Dette er score: " + score);
        return this.score;
    }

    public String getNameFromUID(String UID){
        isDone = false;
        DatabaseReference usersRef = database.getReference().child("users").child(UID);
        DatabaseReference scoreRef = usersRef.child("/Mail");
        scoreRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    isDone = true;
                } else {
                    Object username = task.getResult().getValue();
                    nameFromUID = username.toString();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    isDone = true;
                }
            }
        });
        while(!isDone) {
            System.out.println("Getting the username! ");
        }
        return nameFromUID;

    }

    public ArrayList<String> getUserIDs(){
        isDone = false;
        userIDs = new ArrayList<>();
        DatabaseReference gameRef = database.getReference().child("/users");
        System.out.println( "Dette er gameRef : " + gameRef);
        gameRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    isDone=true;
                }else{
                    Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                    userIDs.addAll(map.keySet());
                    System.out.println("Dette er userIDS: " + userIDs);
                    isDone=true;
                }
                isDone=true;
            }
        }
        );

        while(!isDone){
            System.out.println( "Getting highscores userIDs : " + this.userIDs);
            System.out.println( "Getting highscores");
        }

        return this.userIDs;


    }

    public HashMap<Integer, String> getScoreMap(){
        return this.scoreMap;
    }











    public boolean getIsDone(){
        return this.isDone;
    }


    /**
     *
     *
     * @return
     */
    public List<Integer> getGameIDs(){
        isDone=false;
        gameIDs = new ArrayList<>();
        DatabaseReference gameRef = database.getReference().child("/Games");
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<Integer, Object> map = (Map<Integer, Object>) dataSnapshot.getValue();
                gameIDs.addAll(map.keySet());
                isDone=true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                isDone=true;
            }
        }
        );
        while (!isDone){
            //waiting:)
            System.out.println("Dont delete me");
        }
        return this.gameIDs;

    }

    /*
    TODO: Disse burde kanskje være protected?
     */

    @Override
    public void storeBeds(List<Bed> beds, int GID) {
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID + "/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        DatabaseReference bedsRef = userRef.child("/Beds");
        int i = 0;
        for (Bed bed : beds) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("pos_x", bed.getPos_x());
            result.put("pos_y", bed.getPos_y());
            result.put("size", bed.getSize());
            result.put("horizontal", bed.isHorizontal());
            result.put("texturePath", bed.getTexturePath());
            Map<String, Object> bedValues = result;
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/bed" + (i+1), bedValues);
            bedsRef.updateChildren(childUpdates);
            i++;
        }
    }

    @Override
    public Map<String, Object> retrieveBeds(int GID) {
        // Getting the UID for the opponent
        isDone = false;
        beds = new HashMap<>();
        List<String> players = this.getPlayers(GID);
        String opUID = "";
        for (int i =0; i < players.size(); i++){
            if(!players.get(i).equals(getUID())){
                opUID = players.get(i);
            }
        }
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID + "/Players/");
        DatabaseReference userRef = playerRef.child(opUID);
        DatabaseReference bedsRef = userRef.child("/Beds");
        bedsRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
                isDone=true;
            }
            else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                beds = map;
                isDone=true;
            }
        });
        while (!isDone){
            System.out.println("geetn those neds");
        }
        return beds;
    }



        /**
         * createGame gets called when user wants to create a game
         * The player that created the game, currentUser, gets automatically added
         * The player is set as ready: false
         * The player that creates the game gets the first turn
         * @param GID - get generated in Game in Model, and used as identifier in database
         */
    //NOTE: alle stedene det står "player1" skal det heller være en reell bruker
    public void createGame(int GID){

        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID+"/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        DatabaseReference forfeitedRef = playerRef.child(this.getUID());

        Map uidData = new HashMap();
        uidData.put("Username", this.getUsername());
        userRef.setValue(uidData);
        Map readyData = new HashMap();
        String displayName[] = this.getUsername().split("@");
        readyData.put(displayName[0],false);
        DatabaseReference readyRef = gameRef.child(GID+"/Ready");
        readyRef.setValue(readyData);

        Map forfeitedGame = new HashMap();
        forfeitedGame.put("Forfeited", false);
        forfeitedRef.updateChildren(forfeitedGame);
        Map turnData = new HashMap();
        turnData.put("Turn",this.getUID());
        DatabaseReference turnRef = gameRef.child(GID+"/Turn");
        turnRef.setValue(turnData);

    }


    /**
     * joinGame gets called when a player joins a game and writes the gamePIN
     * The player that joined the game, currentUser, gets automatically added
     * The player is set as ready: false
     * @param gameID - same gamePIN as a created game you want to join
     */
    public void joinGame(int gameID) {
        // TODO  lage disse globale? brukes jo overalt?
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID + "/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        Map uidData = new HashMap();
        uidData.put("Username", this.getUsername());
        userRef.setValue(uidData);
        Map readyData = new HashMap();
        String displayName[] = this.getUsername().split("@");
        readyData.put(displayName[0], false);
        DatabaseReference readyRef = gameRef.child(gameID + "/Ready");
        readyRef.updateChildren(readyData);

        //Added forfeited if the players joins the game
        DatabaseReference forfeitedRef = playerRef.child(this.getUID());
        Map forfeitedGame = new HashMap();
        forfeitedGame.put("Forfeited", false);
        forfeitedRef.updateChildren(forfeitedGame);
    }



    /**
     * Deletes the game when a player exits or quits
     * @param gamePIN
     */
    public void leaveGame(int gamePIN){
        DatabaseReference gameRef = database.getReference().child("/Games");
        gameRef.child(String.valueOf(gamePIN)).removeValue();
    }

    @Override
    public void forfeitedGame(int gamePin) {
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gamePin+"/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        DatabaseReference forfeitedRef = userRef.child("/Forfeited");
        forfeitedRef.setValue(true);
    }


    public List<String> getPlayers(int gameID){
        System.out.println("this is the size"+players.size());
        //only get players from database if the size is less than 2
        // it will never change after there is 2 players there, so then you just keep it locally
        if (players.size() < 2) {
            isDone = false;
            DatabaseReference gameRef = database.getReference().child("/Games");
            DatabaseReference playerRef = gameRef.child(gameID + "/Players/");
            List<String> foundPlayers = new ArrayList<>();
            playerRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                        isDone = true;
                    } else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                        System.out.println(map);
                        /*for (String player : map.keySet()){
                            if (!players.contains(player)){
                                players.add(player);
                            }
                        }*/
                        foundPlayers.addAll(map.keySet());
                        System.out.println("key: " + players);
                        isDone = true;

                    }
                }

            });
            while (!isDone) {
                //waiting
                System.out.println("please be done"); //don't remove
            }
            this.players=foundPlayers;

        }
        return this.players;
    }

    public void clearPlayers(){
        System.out.println("clearingPlayers");
        this.players = new ArrayList<>();
    }

    public void OpHasForfeited(int gamePin){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gamePin+"/Players/");
        List<String> players = getPlayers(gamePin);
        for (String player : players) {
            if (!player.equals(this.getUID())){
                DatabaseReference userRef = playerRef.child(player);
                DatabaseReference forfeitedRef = userRef.child("/Forfeited");
                forfeitedRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            String value = String.valueOf(task.getResult().getValue());
                            setOpHasForfeited(value);
                        }
                    }

                });
            }
        }
    }

    private boolean Forfeited = false;
    public void setOpHasForfeited(String forfeited){
         Forfeited = Boolean.parseBoolean(forfeited);
    }

    public boolean getOpHasForfeited(){
        return Forfeited;
    }


    /**
     * ready gets called when a user presses ready to say that the game can start
     * @param GID the gamePin ID
     */
    public void setPlayerReady(int GID){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID+"/Ready");
        Map<String, Object> updates = new HashMap<>();
        String[] displayName = this.getUsername().split("@");
        updates.put(displayName[0], true);
        playerRef.updateChildren(updates);
    }

    public List<Boolean> getPlayersReady(int GID) {
        isDone=false;
        System.out.println("Ready start FBIC: "+playersReady);
        playersReadyList = new ArrayList<>();
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference readyRef = gameRef.child(GID+"/Ready/");
        readyRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
                isDone = true;
            } else {
                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                Map<String, Boolean> map = (Map<String, Boolean>) task.getResult().getValue();
                map.values();
                System.out.println(map.values());
                this.playersReadyList.addAll(map.values());
                System.out.println("NEST SITE: " + playersReady);
                System.out.println("key: " + players);
                isDone = true;
            }
        });
        while (!isDone) {
            //waiting
            System.out.println("please be done"); //don't remove
        }
        System.out.println("LAST LIST:" + this.playersReadyList);
        return this.playersReadyList;
    }


    public void setTurnToOtherPlayer(int GID){
        System.out.println("setTurnToOtherPlayer in connector");
        DatabaseReference gameRef = database.getReference().child("/Games");
        List<String> players = getPlayers(GID);
        //har en liste med players
        //sjekker hvem som allerede har turn
        //String turn = getTurn(GID);
        //System.out.println("Turn in connector: "+turn);
        //Compare to myself as we KNOW it will the other players turn??
        for (String name : players){
            System.out.println("Name: "+name);
            String uid = this.getUID();
            System.out.println("Uid: "+uid);
            if (!name.equals(uid)){
                Map turnData = new HashMap();
                turnData.put("Turn", name);
                System.out.println("Its their turn");
                DatabaseReference turnRef = gameRef.child(GID+"/Turn");
                turnRef.setValue(turnData);
                break;
            } else {
                System.out.println("Its not their turn");
            }
        }
        System.out.println("Turn after for loop: "+getTurn(GID));
    }

    private void setPlayerTurn(String name){
        this.playerTurn = name;
    }

    /**
     * getTurn checks which players turn it is right now
     * @param gameID
     * @return String UID of player that
     */
    public String getTurn(int gameID){
        //TODO: Implement check for if this is yooou
        System.out.println("GAME ID: " + gameID);
        isDone = false;
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Turn");
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                if (map != null){
                    for (String name : map.values()) {
                        setPlayerTurn(name);
                    }
                }

                isDone=true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                isDone=true;
            }
        });
        while (!isDone){
            //waiting
            System.out.println("Dont delete me");
        }
        return this.playerTurn;
    }

    public boolean isMyTurn(int gameID){
        String turn = getTurn(gameID);
        String name = this.getUID(); //returnere
        System.out.println("Turn: "+turn);
        System.out.println("Name: "+name);
        System.out.println("Is my turn: "+name.equals(turn));
        return name.equals(turn);
    }

    //TODO: spillhåndtering - se forslag til database metoder videre
    //Disse skal bli brukt for å hente og sette data, all logikk knyttet til de skjer i andre klasser
    //Tanken er når man prøver å gjøre et move tar man getPlacement fra den andre spilleren og sjekker om det overlapper
    //sjekker dine egne moves om du har trykket der før, hva det er ved siden av ect.

    /**
     * Når en spiller gjør et move må det legges inn under spilleren i databasen, under moves
     * @param GID
     */
    public void setMove(Square square, int GID){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID + "/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        DatabaseReference movesRef = userRef.child("/Moves");

        HashMap<String, Object> result = new HashMap<>();
        result.put("pos_x", square.getX());
        result.put("pos_y", square.getY());
        result.put("Flower", square.hasFlower());
        result.put("Size", square.getSide());

        Map<String, Object> moveValues = result;
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Move"+moveCount, moveValues);
        moveCount++;
        movesRef.updateChildren(childUpdates);
    }

    public void getOpMoveCount(int GID){
        isDone = false;
        List<String> players = this.getPlayers(GID);
        String opUID = "";
        for (int i =0; i < players.size(); i++){
            if(!players.get(i).equals(getUID())){
                opUID = players.get(i);
            }
        }
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID + "/Players/");
        DatabaseReference userRef = playerRef.child(opUID);
        DatabaseReference movesRef = userRef.child("/Moves");
        movesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    isDone = true;
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                    if(map!=null){
                        opMoveCount = map.keySet().size();
                        System.out.println("Opmovecount er: " + opMoveCount);
                    }
                    isDone = true;
                }
            }

        });
        while (!isDone){
            System.out.println("getting moves");
        }
        System.out.println("Opmovecount er: " + opMoveCount);
    }

    public Map<String, Object> getHit(int GID){
        isDone = false;
        hit = new HashMap<>();
        getOpMoveCount(GID);
        List<String> players = this.getPlayers(GID);
        String opUID = "";
        for (int i =0; i < players.size(); i++){
            if(!players.get(i).equals(getUID())){
                opUID = players.get(i);
            }
        }
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID + "/Players/");
        DatabaseReference userRef = playerRef.child(opUID);
        DatabaseReference movesRef = userRef.child("/Moves/Move"+opMoveCount);
        movesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    isDone = true;
                    hit = null;
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                    if (map == null){
                        hit = null;
                    }
                    else {
                        setHit(map);
                        //hit = map;
                    }
                    isDone = true;
                }
            }

        });
        while (!isDone){
            System.out.println("getting moves");
        }
        //System.out.println("This is the hit"+hit);
        return hit;
    }
    public void setHit(Map<String, Object> map){
        this.hit = map;
        System.out.println("This is the hit"+hit);
    }




    /**
     * Man må kunne sette (i en liste feks) hvor man har plassert blomsterbedene
     * @param gamePIN
     */
    public void setPlacements(int gamePIN){

    }
    /**
     * Man må kunne hente den andre spilleren sin liste med hvor man har plassert blomsterbedene
     * @param gamePIN
     * @return List (forslag)
     */
    public void getPlacements(int gamePIN){

    }


    public FirebaseDatabase getDatabase(){
        return this.database;
    }
    public FirebaseAuth getAuth(){
        return this.mAuth;
    }

}