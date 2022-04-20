package com.mygdx.game.model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mygdx.game.FireBaseInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygdx.game.Model.Game;
import com.mygdx.game.Model.Player;


import static android.content.ContentValues.TAG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * fireBaseConnector connects the application to the Firebase Realtime Database and implements the
 * methods describes in FireBaseInterface from the core module.
 * This class handles all database activity
 */

public class fireBaseConnector implements FireBaseInterface {
     private FirebaseDatabase database;
     private DatabaseReference myRef;
     private FirebaseAuth mAuth;


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
     * @param username
     * @param password
     */
    public void newPlayer(String username, String password) {
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, the user is now both registered and signed in
                Log.d(TAG, "createUserWithEmail:success");
                //TODO: maybe send the user back to the player class to get id? otherwise remove
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference usersRef = database.getReference().child("/users");
                //TODO: put in whole object simultaneously
                // database.getReference().child("/users/"+ "UID").setValue(user.getUid());
                //DatabaseReference uidRef = usersRef.child("/UID");
                usersRef.setValue(user.getUid());
                DatabaseReference mailRef = usersRef.child(getUID()+"/Mail");
                mailRef.setValue(user.getEmail());
                //database.getReference().child("/users/" + user.getUid()).child("/Mail").setValue(user.getEmail());


            } else {
                // Sign in failed
                //TODO: send message back to register class for error handling
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
            }
        });
    }

    /**
     * method that signs in an existing user
     * @param username
     * @param password
     */
    public void signIn(String username, String password){
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success,
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        Log.d(TAG, user.getEmail());

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());}

                });

    }

    /**public void writeUserDataToDb(Player player){
        DatabaseReference usersRef = database.getReference().child("users");
        usersRef.setValue(player);
    }**/

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


    //TODO: hvordan fikse dette?
    /*public boolean emailAlreadyInUse(String email){
        UserRecord userRecord = mAuth.getUserByEmail(email);
    }*/

    /*
    TODO: Disse burde kanskje være protected?
     */

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
        DatabaseReference playerRef = gameRef.child(GID+"/Players");
        Map userData = new HashMap();
        userData.put("Username", "player1");
        playerRef.setValue(userData);
        Map readyData = new HashMap();
        readyData.put("player1",false);
        DatabaseReference readyRef = gameRef.child(GID+"/Ready");
        readyRef.setValue(readyData);
        //TODO: fikse så denne ikke overskriver alle de andre
        /*Map turnData = new HashMap();
        turnData.put("Turn","player1");
        DatabaseReference turnRef = gameRef.child(GID+"");
        turnRef.setValue(turnData);*/
    }


    /**
     * joinGame gets called when a player joins a game and writes the gamePIN
     * The player that joined the game, currentUser, gets automatically added
     * The player is set as ready: false
     * @param gameID - same gamePIN as a created game you want to join
     */
    //TODO: sjekke at spillet man blir med i eksisterer, og ikke har mer enn to brukere
    public void joinGame(int gameID){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Players");
        Map userData = new HashMap();
        userData.put("Username2", "player2");
        playerRef.updateChildren(userData);
        Map readyData = new HashMap();
        readyData.put("player2",false);
        DatabaseReference readyRef = gameRef.child(gameID+"/Ready");
        readyRef.updateChildren(readyData);
        //check user logged in - getID
        //check gamepin - if the same, get user into the game
        ready(gameID, "player2");
    }

    /**
     * Når en bruker har forlatt spillet, ved å trykke exit f.eks, må leave game
     * @param gamePIN
     */
    public void leaveGame(int gamePIN){
        //må slette spillet fra databasen
        //må notifisere den andre spilleren før det skjer (skjer ikke her men i en annen klasse)

    }

    /**
     * ready gets called when a user presses ready to say that the game can start
     * @param gameID
     * @param user
     */
    //TODO: listen for ready - game starts when both users are ready
    // om det er stress her kan vi ha en boolean hjelpemetode

    public void ready(int gameID, String user){
        //når brukeren har trykket bli klar skal
        //sjekker brukeren (helst current user)
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Ready");
        Map<String, Object> updates = new HashMap<>();
        updates.put(user, true);
        playerRef.updateChildren(updates);
    }


    /**
     * When a player makes a move, it updates so its the other players turn
     * @param gameID
     */
    //TODO: listen for turn - kan kun gå videre dersom det er din tur
    // om det er stress her kan vi ha en boolean hjelpemetode - se under
    public void setTurnToOtherPlayer(int gameID){

        //bytter verdi til den andre spilleren i turn
        //må man ha noe sjekk? er bare to brukere så burde jo fint kunne bare bytte
    }

    /**
     * Checks if its your turn
     * Kan evt være en listener som sjekker når turn parameteret blir forandret
     * @param gameID
     * @return
     */
    public boolean yourTurn(int gameID){
        //du kan kun gjøre et move dersom det er din tur
        return false;

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