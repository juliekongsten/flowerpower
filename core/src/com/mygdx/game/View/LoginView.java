package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Controller.LoginController;
import com.mygdx.game.FlowerPowerGame;

public class LoginView extends View {

    private final Texture logo;
    private final Texture login;
    private Stage stage;
    private TextField username;
    private TextField password;
    private final Texture playbook;
    private final Texture settings;
    private final Texture enter_username;
    private final Texture enter_password;
    private final Texture back;
    private String usernameTyped;
    private String passwordTyped;
    private Pixmap cursorColor;
    private LoginController LoginController;

    protected LoginView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        login = new Texture("login.png");
        playbook = new Texture("playbook.png");
        settings = new Texture("settings.png");
        enter_username = new Texture("enter_username.png");
        enter_password = new Texture("enter_password.png");
        back = new Texture("back.png");

        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
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
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Rectangle loginBounds = new Rectangle((float) (FlowerPowerGame.WIDTH/2-(login.getWidth()/2)), 40,
                    login.getWidth(), login.getHeight());
            float settings_x = FlowerPowerGame.WIDTH-playbook.getWidth()-10;
            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle settingsBounds = new Rectangle(settings_x, 15, settings.getWidth(), settings.getHeight());
            if (loginBounds.contains(pos.x, pos.y)) {
                usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);
                //noe form for kontroll på om brukernavn og passord er riktig -> kontrolleren kan gjøre det
                LoginController = new LoginController(usernameTyped, passwordTyped);
                //sende videre til MenuView med innlogget bruker
                vm.set(new MenuView(vm));
            }
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth(), back.getHeight());
            if (backBounds.contains(pos.x, pos.y)) {
                vm.set(new StartView(vm));
            }
            if (playbookBounds.contains(pos.x, pos.y)) {
                //vm.set(new PlaybookView(vm));
                System.out.println("Playbook pressed");
            }
            if (settingsBounds.contains(pos.x, pos.y)) {
                //vm.set(new SettingsView(vm));
                System.out.println("Settings pressed");
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
        sb.draw(login, (float) ((FlowerPowerGame.WIDTH/2)-(login.getWidth()/2)),40);
        sb.draw(playbook, 10, 15);
        float settings_x = FlowerPowerGame.WIDTH-settings.getWidth()-10;
        sb.draw(settings, settings_x, 15);
        // Playbook og settings blir plassert veldig forskjellig i y-retning på desktop og emulator,
        // ikke helt skjønt hvorfor enda
        sb.draw(enter_username, 60,290);
        sb.draw(enter_password,60,190);
        sb.draw(back,10,FlowerPowerGame.HEIGHT-20);
        sb.end();
        stage.draw();
        stage.act();
    }


}
