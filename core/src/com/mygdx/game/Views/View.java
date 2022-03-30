package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class View {
    protected ViewManager vm;
    protected OrthographicCamera cam;

    protected View(ViewManager vm){
        this.vm = vm;
        cam = new OrthographicCamera();
    }
    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);

}
