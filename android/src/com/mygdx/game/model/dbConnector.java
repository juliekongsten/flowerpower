package com.mygdx.game.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dbConnector {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://flowerpower-9b405-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("message");

    public void changeData() {
        myRef.setValue("Hello, World!");
    }


}
