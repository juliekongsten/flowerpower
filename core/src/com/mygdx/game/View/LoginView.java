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
import com.mygdx.game.Controller.ButtonController;
import com.mygdx.game.Controller.PlayerController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class LoginView extends View {

    private final Texture logo;
    private final Texture login;
    private final Stage stage;
    private TextField username;
    private TextField password;
    private final Texture enter_username;
    private final Texture enter_password;
    private final Texture invalidCredentials;
    private final Texture otherMistakeMessage;
    private final ImageButton backButton;
    private Pixmap cursorColor;
    private PlayerController playerController;
    private ButtonController buttonController;

    boolean validCredentials = true;
    boolean otherMistake = false;


    protected LoginView(ViewManager vm) {
        super(vm);
        this.buttonController = new ButtonController();
        logo = new Texture("logo.png");
        login = new Texture("login.png");
        enter_username = new Texture("enter_email.png");
        enter_password = new Texture("enter_password.png");
        invalidCredentials = new Texture("invalidCredential.png");
        otherMistakeMessage = new Texture("wentWrong.png");

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

        backButton = buttonController.getBackButton();
        setBackButtonEvent();
        stage.addActor(backButton);

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
        username.setPosition((float) (FlowerPowerGame.WIDTH/2)-(username.getWidth()/2), 260);
    }

    private void setPasswordField(TextField.TextFieldStyle ts) {
        password = new TextField("", ts);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setWidth(FlowerPowerGame.WIDTH-80);
        password.setHeight(37);
        password.setPosition((float) (FlowerPowerGame.WIDTH/2)-(password.getWidth()/2), 160);
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
            validCredentials=true;
            otherMistake=false;
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Rectangle loginBounds = new Rectangle((float) (FlowerPowerGame.WIDTH/2-(login.getWidth()/2)), 40,
                    login.getWidth(), login.getHeight());
            if (loginBounds.contains(pos.x, pos.y)) {
                String usernameTyped = username.getText();
                System.out.println("Username typed:");
                System.out.println(usernameTyped);
                String passwordTyped = password.getText();
                System.out.println("Password typed:");
                System.out.println(passwordTyped);

                if (usernameTyped.isEmpty()|| passwordTyped.isEmpty()){
                    validCredentials = false;
                } else {
                    try {
                        this.playerController = new PlayerController();
                        playerController.logIn(usernameTyped, passwordTyped);
                        vm.set(new MenuView(vm));

                    }
                    catch (Exception e) {
                        if (e.toString().equals("Invalid email/password")) {
                            validCredentials = false;

                        } else {
                            System.out.println(e.getMessage());
                            otherMistake = true;
                        }
                    }

                }}
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
        sb.draw(login, (float) ((FlowerPowerGame.WIDTH/2)-(login.getWidth()/2)),40);
        sb.draw(enter_username, 60,310);
        sb.draw(enter_password,60,210);
        if (!validCredentials){
            sb.draw(invalidCredentials, (float)
                    (FlowerPowerGame.WIDTH/2-invalidCredentials.getWidth()/2),120);
        } else if (otherMistake){
            sb.draw(otherMistakeMessage, (float)
                    (FlowerPowerGame.WIDTH/2-otherMistakeMessage.getWidth()/2), 120);
        }
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

}
