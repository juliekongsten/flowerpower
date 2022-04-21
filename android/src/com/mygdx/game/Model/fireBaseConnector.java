package com.mygdx.game.Model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
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




import static android.content.ContentValues.TAG;

import java.util.HashMap;
import java.util.Map;

/**
 * fireBaseConnector connects the application to the Firebase Realtime Database and implements the
 * methods describes in FireBaseInterface from the core module.
 * This class handles all database activity
 */

public class fireBaseConnector implements FireBaseInterface {
     private FirebaseDatabase database;
     private DatabaseReference myRef;
     private FirebaseAuth mAuth;
     private Exception exception = null;
     private boolean ready = false;


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
        ready = false;

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

            }
            ready = true;

        });

    }

    /**
     * method that signs in an existing user
     * @param username
     * @param password
     */
    public void signIn(String username, String password){
        this.exception = null;
        this.ready = false;
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success,
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, user.getEmail());

                    }
                    else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        //Thrown when one or more of the credentials passed to a method fail to
                        // identify and/or authenticate the user subject of that operation.
                        //--> email OR password wrong
                        this.exception = new CustomException("Invalid email/password");

                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());}
                    ready = true;
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

    public boolean getReady(){
        return this.ready;
    }


}