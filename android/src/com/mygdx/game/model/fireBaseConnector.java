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

    //TODO: opprette et game - tenker egt at man skal ta inn player som er current user her
    public void createGame(int GID){
        //oppretter et nytt spill inni games
        // TODO: kan fjerne game?
        // Game game = new Game();
        DatabaseReference gameRef = database.getReference().child("/Games");
        //gameRef.setValue(game.getGID());
        // TODO: kan fjerne int GID ettersom den blir sendt som parameter?
        //int GID = game.getGID();
        DatabaseReference playerRef = gameRef.child(GID+"/Players");
        playerRef.push().setValue("player1");
    }


    
    //TODO: bli med i spill
    public void joinGame(int gameID){
        DatabaseReference gameRef = database.getReference().child("/Games");
        DatabaseReference playerRef = gameRef.child(gameID+"/Players");
        playerRef.push().setValue("player2");
        //check user logged in - getID
        //check gamepin - if the same, get user into the game
    }

    public FirebaseDatabase getDatabase(){
        return this.database;
    }
    public FirebaseAuth getAuth(){
        return this.mAuth;
    }



}