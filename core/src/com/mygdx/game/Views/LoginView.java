package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.FlowerPowerGame;

public class LoginView extends View {

    private Texture logo;
    private Texture login;
    private Stage stage;
    private TextField username;
    private TextField password;
    private String usernameTyped;
    private String passwordTyped;

    protected LoginView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        login = new Texture("login.png");

        stage = new Stage();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.background = new Image(new Texture("inputbox.png")).getDrawable();
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        username = new TextField("", style);
        username.setWidth(FlowerPowerGame.WIDTH-20);
        username.setHeight(37);
        username.setPosition(10, 240);
        stage.addActor(username);

        password = new TextField("", style);
        password.setWidth(FlowerPowerGame.WIDTH-20);
        password.setHeight(37);
        password.setPosition(10, 140);
        stage.addActor(password);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            Rectangle loginBounds = new Rectangle(FlowerPowerGame.WIDTH/2-login.getWidth()/2, 40, login.getWidth(), login.getHeight());
            if (loginBounds.contains(x, y)) {
                usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);
                //noe form for kontroll på om brukernavn og passord er riktig -> kontrolleren kan gjøre det
                //sende videre til MenuView med innlogget bruker
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

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
        stage.draw();
        stage.act();
    }


}
