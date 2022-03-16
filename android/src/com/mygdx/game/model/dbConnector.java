package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.db;

public class dbConnector implements db {
     private FirebaseDatabase database;
     private DatabaseReference myRef;

     public dbConnector() {
         database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app");
         myRef = database.getReference("message");
     }
     //FirebaseDatabase database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app/");
     //DatabaseReference myRef = database.getReference("message");
    


    @Override
    public void writeToDb(String message) {
        Gdx.app.log("android", message);
        myRef.setValue(message);
    }
}
