package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class LoginView extends View {

    private Texture logo;
    private Texture login;

    protected LoginView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        login = new Texture("login.png");
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,375);
        sb.draw(login, FlowerPowerGame.WIDTH/2-login.getWidth()/2,40);
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.3);
        font.setColor(Color.BLACK);
        font.draw(sb, "Enter username",80,315);
        font.draw(sb,"Enter password",80,215);
        sb.end();
    }


}
