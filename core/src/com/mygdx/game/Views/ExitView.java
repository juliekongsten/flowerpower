package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class ExitView extends View{

    private final Texture logo;
    private Texture gameOver;


    protected ExitView(ViewManager vm) {
        super(vm);
        this.logo = new Texture("small_logo.png");
        this.gameOver = new Texture("game_over.png");
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
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo, FlowerPowerGame.WIDTH/2-logo.getWidth()/2,FlowerPowerGame.HEIGHT-logo.getHeight()-20);
        sb.draw(gameOver,FlowerPowerGame.WIDTH/2-gameOver.getWidth()/2,400);
        sb.end();

    }
}
