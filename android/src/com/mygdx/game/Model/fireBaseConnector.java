package com.mygdx.game.Model;

import androidx.annotation.NonNull;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.mygdx.game.FireBaseInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygdx.game.Model.CustomException;




import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.Collection;
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
     private List<String> players;
     private List<Integer> gameIDs;
     private Map<String, Object> beds;
     private boolean playersReady = true;


    /**
     * Constructor that gets an instance of the database and authorization
     */
    public fireBaseConnector() {
        database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app");
        mAuth = FirebaseAuth.getInstance();
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
                Map<String, String> userData = new HashMap<String, String>();
                userData.put("Mail", user.getEmail());
                // TODO: legge inn score
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


    //TODO: hvordan fikse dette?
    /*public boolean emailAlreadyInUse(String email){
        UserRecord userRecord = mAuth.getUserByEmail(email);
    }*/

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
                //System.out.println(map);
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
        bedsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                beds = map;
                isDone=true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                isDone=true;
            }
        });
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





        //TODO: fikse så denne ikke overskriver alle de andre
        Map turnData = new HashMap();
        turnData.put("Turn",this.getUID());
        DatabaseReference turnRef = gameRef.child(GID+"/Turn");
        turnRef.setValue(turnData);
        //setTurnToOtherPlayer(GID);
        //getPlayers(GID);
        //List<Integer> IDs = getGameIDs();
        //System.out.println(IDs);
    }


    /**
     * joinGame gets called when a player joins a game and writes the gamePIN
     * The player that joined the game, currentUser, gets automatically added
     * The player is set as ready: false
     * @param gameID - same gamePIN as a created game you want to join
     */
    //TODO: sjekke at spillet man blir med i eksisterer, og ikke har mer enn to brukere

    //TODO: redudant kode
    public void joinGame(int gameID){
         // TODO  lage disse globale? brukes jo overalt?
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        Map uidData = new HashMap();
        uidData.put("Username", this.getUsername());
        userRef.setValue(uidData);
        Map readyData = new HashMap();
        String displayName[] = this.getUsername().split("@");
        readyData.put(displayName[0],false);
        DatabaseReference readyRef = gameRef.child(gameID+"/Ready");
        readyRef.updateChildren(readyData);
        //check user logged in - getID
        //check gamepin - if the same, get user into the game
        //ready(gameID,displayName[0]);
        //setTurnToOtherPlayer(gameID);
    }



    /**
     * Deletes the game when a player exits or quits
     * @param gamePIN
     */
    public void leaveGame(int gamePIN){
        DatabaseReference gameRef = database.getReference().child("/Games");
        gameRef.child(String.valueOf(gamePIN)).removeValue();
    }

    public void forfeitedGame(int gamePin){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gamePin+"/Players/");
        DatabaseReference userRef = playerRef.child(this.getUID());
        DatabaseReference forfeitedRef = userRef.child("/Forfeited");
        forfeitedRef.setValue(true);
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

    @Override
    public boolean getOpHasForfeited(){
        return Forfeited;
    }


    /**
     * ready gets called when a user presses ready to say that the game can start
     * @param GID the gamePin ID
     */


    @Override
    public void setPlayerReady(int GID){
        System.out.println("Kommer hit");
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(GID+"/Ready");
        Map<String, Object> updates = new HashMap<>();
        String[] displayName = this.getUsername().split("@");
        updates.put(displayName[0], true);
        playerRef.updateChildren(updates);
    }

    @Override
    public boolean getPlayersReady(int GID) {
        return false;
    }


    /**
     * getPlayers gets all the in the game with this gameID
     * @param gameID
     * @return List<String> for player UID
     */
    //TODO: legge inn før join game så man sjekker at listen er allerede full

    public List<String> getPlayers(int gameID){
        isDone=false;
        players = new ArrayList<>();
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Players/");
        playerRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, Object> map = (Map<String, Object>) task.getResult().getValue();
                    System.out.println(map);
                    players.addAll(map.keySet());
                    System.out.println("key: " + players);
                    isDone=true;
                }
            }

        });
    /*playerRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
            System.out.println(map);
            players.addAll(map.keySet());
            System.out.println("key: " + players);
            isDone=true;
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
            isDone=true;
        }
    });*/
        //mDatabase.child("users").child(userId).get();
        //bytter verdi til den andre spilleren i turn
        //må man ha noe sjekk? er bare to brukere så burde jo fint kunne bare bytte
        while(!isDone){
            //waiting
            System.out.println("Dont delete me");
        }
        return players;
    }


    public void setTurnToOtherPlayer(int gameID){
        DatabaseReference gameRef = database.getReference().child("/Games");
        List<String> players = getPlayers(gameID);
        //har en liste med players
        //sjekker hvem som allerede har turn
        for (String name : players){
            if (name != getTurn(gameID)){
                Map turnData = new HashMap();
                turnData.put("Turn", name);
                DatabaseReference turnRef = gameRef.child(gameID+"/Turn");
                turnRef.setValue(turnData);
                return;
            }
        }
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
        isDone = false;
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Turn");
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                for (String name : map.values()) {
                    setPlayerTurn(name);
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
        String name = getUID();

        //System.out.println("Is my turn: "+name.equals(getTurn(gameID)));
        return name.equals(turn);
    }

    //TODO: spillhåndtering - se forslag til database metoder videre
    //Disse skal bli brukt for å hente og sette data, all logikk knyttet til de skjer i andre klasser
    //Tanken er når man prøver å gjøre et move tar man getPlacement fra den andre spilleren og sjekker om det overlapper
    //sjekker dine egne moves om du har trykket der før, hva det er ved siden av ect.

    /**
     * Når en spiller gjør et move må det legges inn under spilleren i databasen, under moves
     * @param gamePIN
     */
    public void setMove(int gamePIN){

    }

    /**
     * Man må kunne hente alle movsene til den andre spilleren
     * @param gamePIN
     * @return List (forslag)
     */
    public void getMoves(int gamePIN){
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