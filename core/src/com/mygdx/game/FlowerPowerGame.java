package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Views.RegisterView;
import com.mygdx.game.Views.StartView;
import com.mygdx.game.Views.ViewManager;

/**
 * FlowerPowerGame
 */


public class FlowerPowerGame extends ApplicationAdapter {
	public static final int WIDTH = 375;
	public static final int HEIGHT = 667;
	private SpriteBatch batch;
	private ViewManager vm;
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
		vm = ViewManager.getViewManager();
		vm.push(new StartView(vm));
		//calls methods in fireBaseConnector to test them out
		_FBIC.writeToDb("message","tir!");
		_FBIC.readFromDb();
	}

	@Override
	public void render () {
		/*ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
		vm.render(batch);
		vm.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
