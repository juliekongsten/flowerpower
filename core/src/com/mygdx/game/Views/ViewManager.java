package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class ViewManager {
    private Stack<View> views;

    private static final ViewManager viewmanager = new ViewManager();

    private ViewManager(){
        views = new Stack<>();
    }

    public static ViewManager getViewManager(){
        return viewmanager;
    }

    public void push(View state){
        views.push(state);
    }

    public void pop(){
        views.pop();
    }

    public void set(View state){
        views.pop();
        views.push(state);
    }

    public void update(float dt){
        views.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        views.peek().render(sb);
    }

}
