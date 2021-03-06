package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

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
        if(Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle loginBounds = new Rectangle(125, 100, login.getWidth(), login.getHeight());
            Rectangle registerBounds = new Rectangle(100, 175, register.getWidth(), register.getHeight());
            if (loginBounds.contains(pos.x, pos.y)) {
                System.out.println("LOGIN PRESSED!");
                vm.set(new LoginView(vm));
            }
            if (registerBounds.contains(pos.x, pos.y)) {
                System.out.println("REGISTER PRESSED!");
                vm.set(new RegisterView(vm));
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,375);
        sb.draw(register,100,175);
        sb.draw(login,125,100);
        sb.end();




    }
}
