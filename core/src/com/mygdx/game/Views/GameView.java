package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class GameView extends View{

    private final Texture pool;
    private Texture ready;


    protected GameView(ViewManager vm) {
        super(vm);
        pool = new Texture("pool.png");
        ready = new Texture("Button.png");
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(ready, 100, 50);
        //sb.draw(ready,(float) (FlowerPowerGame.WIDTH/2-ready.getWidth()/2), (float) 100 );
        //sb.draw(pool, (float) (FlowerPowerGame.WIDTH/2-pool.getWidth()/2),(float)(FlowerPowerGame.HEIGHT/2-pool.getHeight()/2));
        sb.draw(pool, 10, 200);
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);
        sb.end();

    }
}
