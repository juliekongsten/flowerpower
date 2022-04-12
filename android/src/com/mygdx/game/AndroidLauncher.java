package com.mygdx.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mygdx.game.Model.Player;
import com.mygdx.game.model.fireBaseConnector;

public class AndroidLauncher extends AndroidApplication {

	private fireBaseConnector fireBaseConnector;
	private FirebaseAuth mAuth;
	private Player player;
	private DatabaseReference reference;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.fireBaseConnector = new fireBaseConnector();
		initialize(new FlowerPowerGame(fireBaseConnector), config);
		// Check if user is signed in (non-null) and update UI accordingly.

	}




}
