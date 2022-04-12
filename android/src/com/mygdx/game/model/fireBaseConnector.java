package com.mygdx.game.model;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mygdx.game.FireBaseInterface;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import static android.content.ContentValues.TAG;

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
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());}

                });

    }

    public String getUsername(){
        FirebaseUser user = mAuth.getCurrentUser();
        return user.getEmail();
    }

    /*public boolean emailAlreadyInUse(String email){
        UserRecord userRecord = mAuth.getUserByEmail(email);
    }*/

    /*
    TODO: Disse burde kanskje være protected?
     */

    public FirebaseDatabase getDatabase(){
        return this.database;
    }
    public FirebaseAuth getAuth(){
        return this.mAuth;
    }



}