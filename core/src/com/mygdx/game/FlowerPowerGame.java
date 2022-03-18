package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;


public class FlowerPowerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	FireBaseInterface _FBIC;

	public FlowerPowerGame(FireBaseInterface FBIC ){
		_FBIC = FBIC;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		_FBIC.writeToDb("message","fredag!");
		_FBIC.readFromDb();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		//writeToDb("Hello, world!");
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	/*public void writeToDb(String message) {
		Gdx.app.log("core", message);
		database.writeToDb(message);
	}*/
}
