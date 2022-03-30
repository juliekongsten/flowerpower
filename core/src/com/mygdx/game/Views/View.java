package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class View {
    protected ViewManager vm;

    protected View(ViewManager vm){
        this.vm = vm;
    }
    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);

}
