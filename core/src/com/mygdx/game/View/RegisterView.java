package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.Controller.PlayerController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class RegisterView extends View {
    private final Texture logo;
    private final Texture register;
    private final Texture enter_username;
    private final Texture enter_password;
    private final Texture password_again;
    private final Texture passwordMessage;
    private final Texture weakPasswordMessage;
    private final Texture invalidEmailMessage;
    private final Texture usernameTakenMessage;
    private final Texture otherMistakeMessage;
    private final Stage stage;
    private TextField username;
    private TextField password;
    private TextField passwordCheck;
    private Pixmap cursorColor;
    private final ImageButton highscoreButton;
    private final ImageButton playbookButton;
    private final ImageButton backButton;



    private boolean passwordMatch = true;
    private boolean strongPassword = true;
    private boolean validEmail = true;
    private boolean newEmail = true;
    private boolean otherMistake = false;
    private PlayerController playerController;
    private GameController gameController; //not used



    public RegisterView(ViewManager vm) {
        super(vm);
        //gameController = new GameController(); //never used
        logo = new Texture("logo.png");
        register = new Texture("register.png");
        enter_username = new Texture("enter_email.png");
        enter_password = new Texture("enter_password.png");
        password_again = new Texture("password_again.png");
        passwordMessage = new Texture("passwordMatch.png");
        weakPasswordMessage = new Texture("weakPassword.png");
        invalidEmailMessage = new Texture("invalidEmail.png");
        usernameTakenMessage = new Texture("usernameTaken.png");
        otherMistakeMessage = new Texture("wentWrong.png");

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

        Button playbook = new Button("playbook.png", 10, 15);
        playbookButton = playbook.getButton();
        setPlaybookButtonEvent();
        stage.addActor(playbookButton);

        Button highscore = new Button("Highscore.png", FlowerPowerGame.WIDTH - 125, 15);
        highscoreButton = highscore.getButton();
        setHighscoreButtonEvent();
        stage.addActor(highscoreButton);

        Button back = new Button("back.png", 20, FlowerPowerGame.HEIGHT - 20);
        backButton = back.getButton();
        setBackButtonEvent();
        stage.addActor(backButton);
    }

    private void setPlaybookButtonEvent() {
        playbookButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                //vm.set(new PlaybookView(vm));
                System.out.println("PLAYBOOK");
                return true;
            }
        });
    }

    private void setHighscoreButtonEvent() {
        highscoreButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                vm.set(new HighscoreView(vm));
                return true;
            }
        });
    }

    private void setBackButtonEvent() {
        backButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                vm.set(new StartView(vm));
                return true;
            }
        });
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
            if (registerBounds.contains(pos.x, pos.y)) {
                // Sende inn til databasen ny bruker
                passwordMatch=true;
                validEmail=true;
                strongPassword=true;
                newEmail=true;
                otherMistake=false;

                String usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                String passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);
                String passwordCheckTyped = passwordCheck.getText();
                System.out.println("Password check typed: \n" + passwordCheckTyped);
                // Sjekke at passordene stemmer overens og hvis de gjør det, send videre til Registercontroller og player


                if (passwordTyped.equals(passwordCheckTyped)){
                    // Sende videre til MenuView med innlogget bruker
                    // sendes videre for å sjekke med db
                    try {
                        //call registercontroller and try to make user
                        this.playerController = new PlayerController();
                        playerController.register(usernameTyped, passwordTyped);
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
        sb.draw(enter_username,60,345);
        sb.draw(enter_password,60,275);
        sb.draw(password_again,60,200);
        if (!passwordMatch){
            sb.draw(passwordMessage, (float) (FlowerPowerGame.WIDTH/2-passwordMessage.getWidth()/2),130);
        } else if (!newEmail) {
            sb.draw(usernameTakenMessage, (float) (FlowerPowerGame.WIDTH/2-usernameTakenMessage.getWidth()/2),130);
        } else if (!validEmail){
            sb.draw(invalidEmailMessage, (float) (FlowerPowerGame.WIDTH/2-invalidEmailMessage.getWidth()/2), 130);
        } else if (!strongPassword){
            sb.draw(weakPasswordMessage, (float) (FlowerPowerGame.WIDTH/2-weakPasswordMessage.getWidth()/2),130);
        } else if (otherMistake){
            sb.draw(otherMistakeMessage,(float) (FlowerPowerGame.WIDTH/2-otherMistakeMessage.getWidth()/2),130);
        }
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
