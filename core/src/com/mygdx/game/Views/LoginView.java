package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.FlowerPowerGame;

import java.awt.event.MouseAdapter;

public class LoginView extends View {

    private Texture logo;
    private Texture login;
    private Stage stage;
    private TextField username;
    private TextField password;
    private String usernameTyped;
    private String passwordTyped;
    private Pixmap cursorColor;

    protected LoginView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        login = new Texture("login.png");

        stage = new Stage();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = new Image(new Texture("inputbox.png")).getDrawable();
        textFieldStyle.font = new BitmapFont();
        textFieldStyle.fontColor = Color.BLACK;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();

        setCursor(labelStyle);
        textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();

        setUsernameField(textFieldStyle);
        stage.addActor(username);

        setPasswordField(textFieldStyle);
        stage.addActor(password);
    }


    private void setUsernameField(TextField.TextFieldStyle ts) {
        username = new TextField("", ts);
        username.setWidth(FlowerPowerGame.WIDTH-80);
        username.setHeight(37);
        username.setPosition((float) (FlowerPowerGame.WIDTH/2)-(username.getWidth()/2), 240);
    }

    private void setPasswordField(TextField.TextFieldStyle ts) {
        password = new TextField("", ts);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setWidth(FlowerPowerGame.WIDTH-80);
        password.setHeight(37);
        password.setPosition((float) (FlowerPowerGame.WIDTH/2)-(password.getWidth()/2), 140);
    }

    private void setCursor(Label.LabelStyle ls) {
        Label label = new Label("|", ls);
        cursorColor = new Pixmap((int) label.getWidth(),
                (int) label.getHeight(), Pixmap.Format.RGB888);
        cursorColor.setColor(Color.BLACK);
        cursorColor.fill();

    }
    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            Rectangle loginBounds = new Rectangle((float) (FlowerPowerGame.WIDTH/2-(login.getWidth()/2)), 40,
                    login.getWidth(), login.getHeight());
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
        sb.draw(login, (float) ((FlowerPowerGame.WIDTH/2)-(login.getWidth()/2)),40);
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
