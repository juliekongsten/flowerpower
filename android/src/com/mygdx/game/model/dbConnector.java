package com.mygdx.game.model;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.db;

public class dbConnector implements db {
     private FirebaseDatabase database;
     private DatabaseReference myRef;
     private DatabaseReference newRef;

     public dbConnector() {
         database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app");
         myRef = database.getReference("message");
         newRef = database.getReference("read");
     }
     //FirebaseDatabase database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app/");
     //DatabaseReference myRef = database.getReference("message");
    


    @Override
    public void writeToDb(String message) {
        Gdx.app.log("android", message);
        myRef.setValue(message);
    }

    newRef.child("read").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
       @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
              if (!task.isSuccessful()) {
                          Log.e("firebase", "Error getting data", task.getException());
                      } }     
    }