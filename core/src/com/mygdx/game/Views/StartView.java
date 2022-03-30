package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StartView extends View{
    private Texture logo;
    private Texture register;
    private Texture login;



    public StartView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        register = new Texture("register.png");
        login = new Texture("login.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isTouched()){
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);

        }

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,350);
        sb.draw(register,100,175);
        sb.draw(login,125,100);
        sb.end();



    }
}
