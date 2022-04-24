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
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class JoinView extends View {
    private final Texture logo;
    private final Texture pinText;
    private final Texture join;
    private final Texture gameFullMessage;
    private final Texture ownGameMessage;
    private final Texture notExistingMessage;
    private final Stage stage;
    private Pixmap cursorColor;
    private TextField gamePin;
    private GameController gameController;
    private ButtonController buttonController;

    private boolean gameFull;
    private boolean ownGame;
    private boolean notExisting;

    private final ImageButton highscoreButton;
    private final ImageButton playbookButton;
    private final ImageButton backButton;

    protected JoinView(ViewManager vm) {
        super(vm);
        this.buttonController = new ButtonController();
        logo = new Texture("logo.png");
        pinText = new Texture("join_pin.png");
        join = new Texture("join.png");
        gameFullMessage = new Texture("gameFull.png");
        ownGameMessage = new Texture("ownGame.png");
        notExistingMessage = new Texture("invalidPin.png");

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

        setPinField(textFieldStyle);
        stage.addActor(gamePin);

        playbookButton = buttonController.getPlaybookButton();
        setPlaybookButtonEvent();
        stage.addActor(playbookButton);
        highscoreButton = buttonController.getHighscoreButton();
        setHighscoreButtonEvent();
        stage.addActor(highscoreButton);
        backButton = buttonController.getBackButton();
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
                vm.set(new MenuView(vm));
                return true;
            }
        });
    }

    private void setPinField(TextField.TextFieldStyle ts) {
        gamePin = new TextField("", ts);
        gamePin.setWidth(FlowerPowerGame.WIDTH-80);
        gamePin.setHeight(37);
        gamePin.setPosition((float) (FlowerPowerGame.WIDTH/2)-(gamePin.getWidth()/2), 230);
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
        if (Gdx.input.justTouched()) {
            ownGame=false;
            gameFull=false;
            notExisting=false;
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle joinBounds = new Rectangle((FlowerPowerGame.WIDTH/2-join.getWidth()/2), 50, join.getWidth(), join.getHeight());

            if (joinBounds.contains(pos.x, pos.y)) {
                //TODO: Some way to check pin
                //vm.set(new GameView(vm));
                //TODO: sjekk at gamePin.getText() er en int
                /*gameController.joinGame(Integer.parseInt(gamePin.getText()));
                vm.set(new PlaceBedsView(vm));*/
                System.out.println("JOIN WAS PRESSED!");
                try {
                    System.out.println("try");
                    gameController.joinGame(Integer.parseInt((gamePin.getText())));
                    vm.set(new PlaceBedsView(vm));
                } catch (IllegalArgumentException e) {

                    System.out.println("Catch exception: "+e.getMessage());
                    if (e.getMessage().equals("Too many players in game")){
                        gameFull=true;
                    } else if (e.getMessage().equals("Girly u already in")){
                        ownGame=true;
                    } else if (e.getMessage().equals("GameID does not exsist")){
                        notExisting=true;
                    }
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
        sb.draw(logo,36,375);
        sb.draw(pinText, (FlowerPowerGame.WIDTH/2-pinText.getWidth()), 290);
        sb.draw(join, (FlowerPowerGame.WIDTH/2-join.getWidth()/2), 50);
        if (ownGame){
            sb.draw(ownGameMessage, FlowerPowerGame.WIDTH/2-ownGameMessage.getWidth()/2,140);
        } else if (gameFull){
            sb.draw(gameFullMessage, FlowerPowerGame.WIDTH/2-gameFullMessage.getWidth()/2,140);
        } else if (notExisting){
            sb.draw(notExistingMessage, FlowerPowerGame.WIDTH/2-notExistingMessage.getWidth()/2,140);

        }
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
