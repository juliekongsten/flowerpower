package com.mygdx.game.Views;

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
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.Controller.RegisterController;
import com.mygdx.game.FlowerPowerGame;

public class RegisterView extends View {
    private final Texture logo;
    private final Texture register;
    private final Texture playbook;
    private final Texture settings;
    private Stage stage;
    private TextField username;
    private TextField password;
    private TextField passwordCheck;
    private String usernameTyped;
    private String passwordTyped;
    private String passwordCheckTyped;
    private Pixmap cursorColor;
    private RegisterController registerController;
    private GameController gameController;



    public RegisterView(ViewManager vm) {
        super(vm);
        gameController = new GameController();
        logo = new Texture("logo.png");
        register = new Texture("register.png");
        playbook = new Texture("playbook.png");
        settings = new Texture("settings.png");

        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = new BitmapFont();
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = new Image(new Texture("inputbox.png")).getDrawable();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();

        setCursor(labelStyle);
        textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();

        setUsernameField(textFieldStyle);
        stage.addActor(username);

        setPasswordField(textFieldStyle);
        stage.addActor(password);

        setPasswordCheckField(textFieldStyle);
        stage.addActor(passwordCheck);
    }

    private void setUsernameField(TextField.TextFieldStyle ts) {
        username = new TextField("", ts);
        username.setWidth(FlowerPowerGame.WIDTH-80);
        username.setHeight(37);
        username.setPosition((float) (FlowerPowerGame.WIDTH/2)-(username.getWidth()/2), 280);
    }

    private void setPasswordField(TextField.TextFieldStyle ts) {
        password = new TextField("", ts);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setWidth(FlowerPowerGame.WIDTH-80);
        password.setHeight(37);
        password.setPosition((float) (FlowerPowerGame.WIDTH/2)-(password.getWidth()/2), 210);
    }

    private void setPasswordCheckField(TextField.TextFieldStyle ts) {
        passwordCheck = new TextField("", ts);
        passwordCheck.setPasswordMode(true);
        passwordCheck.setPasswordCharacter('*');
        passwordCheck.setWidth(FlowerPowerGame.WIDTH-80);
        passwordCheck.setHeight(37);
        passwordCheck.setPosition((float) (FlowerPowerGame.WIDTH/2)-(passwordCheck.getWidth()/2), 140);
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

            Rectangle registerBounds = new Rectangle((float) (FlowerPowerGame.WIDTH/2-(register.getWidth()/2)),
                    40, register.getWidth(), register.getHeight());
            float settings_x = FlowerPowerGame.WIDTH-playbook.getWidth()-10;
            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle settingsBounds = new Rectangle(settings_x, 15, settings.getWidth(), settings.getHeight());
            if (registerBounds.contains(pos.x, pos.y)) {
                // Sende inn til databasen ny bruker
                usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);
                passwordCheckTyped = passwordCheck.getText();
                System.out.println("Password check typed: \n" + passwordCheckTyped);
                // Sjekke at passordene stemmer overens og hvis de gjør det, send videre til Registercontroller og player
                try{
                if (checkPassword(passwordTyped, passwordCheckTyped)){
                    registerController = new RegisterController(usernameTyped, passwordTyped);
                }}
                catch  (Exception e) {
                    if (e.toString().equals("Email already in use")){

                    }
                    else if(e.toString().equals("Invalid email")){

                    }
                    else if(e.toString().equals("Weak password")){

                    }
                }

                // Sende videre til MenuView med innlogget bruker
                // sendes videre for å sjekke med db


                vm.set(new MenuView(vm));
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

        //TODO: ha med noen beskjed at de ikke matcher
    public boolean checkPassword(String password, String passwordCheckTyped){

        if (password.equals(passwordCheckTyped)){
            System.out.println("Passwords match!");
            return true;
        } else{
            System.out.println("Passwords does not match");
            return false;
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
        sb.draw(register,100,50);
        sb.draw(playbook, 10, 15);
        float settings_x = FlowerPowerGame.WIDTH-settings.getWidth()-10;
        sb.draw(settings, settings_x, 15);
        // Playbook og settings blir plassert veldig forskjellig i y-retning på desktop og emulator,
        // ikke helt skjønt hvorfor enda
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.2);
        font.setColor(Color.BLACK);
        font.draw(sb, "Enter username",80,340);
        font.draw(sb,"Enter password",80,270);
        font.draw(sb,"Enter password again",80,200);
        sb.end();
        stage.draw();
        stage.act();
    }
}
