package com.mygdx.game.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Controller.GameController;

import java.util.Stack;

public class ViewManager {
    private Stack<View> views;
    private GameController controller;

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

    /**
     * Methods for assuring the same controller used in PlaceBedsView and GameView
     * @param controller
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GameController getController(){
        return this.controller;
    }
}
