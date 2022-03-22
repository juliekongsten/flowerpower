package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * FlowerPowerGame
 */


public class FlowerPowerGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	FireBaseInterface _FBIC;

	/**
	 * Constructor that creates instance of FireBaseInterface
	 * @param FBIC
	 */
	public FlowerPowerGame(FireBaseInterface FBIC ){
		_FBIC = FBIC;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//calls methods in fireBaseConnector to test them out
		_FBIC.writeToDb("message","tir!");
		_FBIC.readFromDb();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
