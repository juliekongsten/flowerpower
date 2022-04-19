package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	public static FireBaseInterface _FBIC = null;


	/**
	 * Constructor that creates instance of FireBaseInterface
	 * @param FBIC
	 */
	public FlowerPowerGame(FireBaseInterface FBIC ){
		_FBIC = FBIC;

	}

	public static FireBaseInterface getFBIC(){
		return _FBIC; 
	}



	@Override
	public void create () {
		batch = new SpriteBatch();
		vm = ViewManager.getViewManager();
		vm.push(new StartView(vm));

		//calls methods in fireBaseConnector to test them out
		//TODO: remove
		_FBIC.writeToDb("message","jasså!");
		_FBIC.readFromDb();
		//_FBIC.signIn("testuser2@gmail.com", "123456");
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
