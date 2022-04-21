package com.mygdx.game;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Model.fireBaseConnector;

public class AndroidLauncher extends AndroidApplication {

	private fireBaseConnector fireBaseConnector;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.fireBaseConnector = new fireBaseConnector();
		initialize(new FlowerPowerGame(fireBaseConnector), config);
		// Check if user is signed in (non-null) and update UI accordingly.

	}




}
