package com.mygdx.game.View;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlowerPowerGame;

public abstract class View {
    protected ViewManager vm;
    protected OrthographicCamera cam;

    protected View(ViewManager vm){
        this.vm = vm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT);
    }
    protected abstract void handleInput() throws Exception;
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);

}
