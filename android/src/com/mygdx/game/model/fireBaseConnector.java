package com.mygdx.game.model;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import android.util.Log;
import com.mygdx.game.FireBaseInterface;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static android.content.ContentValues.TAG;

public class fireBaseConnector implements FireBaseInterface {
     private FirebaseDatabase database;
     private DatabaseReference myRef;
     private DatabaseReference newRef;

     public fireBaseConnector() {
         database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app");
     }
     //FirebaseDatabase database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app/");
     //DatabaseReference myRef = database.getReference("message");
    


    @Override
    public void writeToDb(String target, String value){
            myRef = database.getReference(target);
            myRef.setValue(value);
    }

    @Override
    public void readFromDb() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}