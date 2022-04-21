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
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.Controller.RegisterController;
import com.mygdx.game.FlowerPowerGame;

public class RegisterView extends View {
    private final Texture logo;
    private final Texture register;
    private final Texture playbook;
    private final Texture settings;
    private final Texture enter_username;
    private final Texture enter_password;
    private final Texture password_again;
    private final Texture passwordMessage;
    private final Texture weakPasswordMessage;
    private final Texture invalidEmailMessage;
    private final Texture usernameTakenMessage;
    private final Texture otherMistakeMessage;
    private final Texture back;
    private Stage stage;
    private TextField username;
    private TextField password;
    private TextField passwordCheck;
    private String usernameTyped;
    private String passwordTyped;
    private String passwordCheckTyped;
    private Pixmap cursorColor;



    private boolean passwordMatch = true;
    private boolean strongPassword = true;
    private boolean validEmail = true;
    private boolean newEmail = true;
    private boolean otherMistake = false;



    public RegisterView(ViewManager vm) {
        super(vm);
        //gameController = new GameController(); //never used
        logo = new Texture("logo.png");
        register = new Texture("register.png");
        playbook = new Texture("playbook.png");
        settings = new Texture("settings.png");
        enter_username = new Texture("enter_username.png");
        enter_password = new Texture("enter_password.png");
        password_again = new Texture("password_again.png");
        passwordMessage = new Texture("passwordMatch.png");
        weakPasswordMessage = new Texture("weakPassword.png");
        invalidEmailMessage = new Texture("invalidEmail.png");
        usernameTakenMessage = new Texture("usernameTaken.png");
        otherMistakeMessage = new Texture("wentWrong.png");


        back = new Texture("back.png");


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
        username.setPosition((float) (FlowerPowerGame.WIDTH/2)-(username.getWidth()/2), 300);
    }

    private void setPasswordField(TextField.TextFieldStyle ts) {
        password = new TextField("", ts);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setWidth(FlowerPowerGame.WIDTH-80);
        password.setHeight(37);
        password.setPosition((float) (FlowerPowerGame.WIDTH/2)-(password.getWidth()/2), 230);
    }

    private void setPasswordCheckField(TextField.TextFieldStyle ts) {
        passwordCheck = new TextField("", ts);
        passwordCheck.setPasswordMode(true);
        passwordCheck.setPasswordCharacter('*');
        passwordCheck.setWidth(FlowerPowerGame.WIDTH-80);
        passwordCheck.setHeight(37);
        passwordCheck.setPosition((float) (FlowerPowerGame.WIDTH/2)-(passwordCheck.getWidth()/2), 160);
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
                passwordMatch=true;
                validEmail=true;
                strongPassword=true;
                newEmail=true;
                otherMistake=false;

                usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);
                passwordCheckTyped = passwordCheck.getText();
                System.out.println("Password check typed: \n" + passwordCheckTyped);
                // Sjekke at passordene stemmer overens og hvis de gjør det, send videre til Registercontroller og player


                if (passwordTyped.equals(passwordCheckTyped)){


                    // Sende videre til MenuView med innlogget bruker
                    // sendes videre for å sjekke med db
                    try {
                        //call registercontroller and try to make user
                        RegisterController controller = new RegisterController(usernameTyped, passwordTyped);
                        vm.set(new MenuView(vm));

                    } catch (Exception e) {

                        if (e.toString().equals("Email already in use")){
                            newEmail = false;
                        }
                        else if(e.toString().equals("Invalid email")){
                            validEmail = false;
                        }
                        else if(e.toString().equals("Weak password")){
                            strongPassword = false;
                        }
                        else if(usernameTyped.isEmpty()){
                            validEmail = false;
                        }
                        else if (passwordTyped.isEmpty()){
                            strongPassword = false;
                        }
                        else {
                            otherMistake=true;
                        }
                    }

                } else {
                    passwordMatch = false;

                }

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
        sb.draw(logo,36,395);
        sb.draw(register,100,50);
        sb.draw(playbook, 10, 15);
        float settings_x = FlowerPowerGame.WIDTH-settings.getWidth()-10;
        sb.draw(settings, settings_x, 15);
        // Playbook og settings blir plassert veldig forskjellig i y-retning på desktop og emulator,
        // ikke helt skjønt hvorfor enda
        sb.draw(enter_username,60,345);
        sb.draw(enter_password,60,275);
        sb.draw(password_again,60,200);
        if (!passwordMatch){
            sb.draw(passwordMessage, FlowerPowerGame.WIDTH/2-passwordMessage.getWidth()/2,130);
        } else if (!newEmail) {
            sb.draw(usernameTakenMessage, FlowerPowerGame.WIDTH/2-usernameTakenMessage.getWidth()/2,130);
        } else if (!validEmail){
            sb.draw(invalidEmailMessage, FlowerPowerGame.WIDTH/2-invalidEmailMessage.getWidth()/2, 130);
        } else if (!strongPassword){
            sb.draw(weakPasswordMessage, FlowerPowerGame.WIDTH/2-weakPasswordMessage.getWidth()/2,130);
        } else if (otherMistake){
            sb.draw(otherMistakeMessage,FlowerPowerGame.WIDTH/2-otherMistakeMessage.getWidth()/2,130);
        }

        sb.draw(back,10,FlowerPowerGame.HEIGHT-20);
        sb.end();
        stage.draw();
        stage.act();
    }
}
