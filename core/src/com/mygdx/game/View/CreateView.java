package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class CreateView extends View {

    // DET FUNKER IKKE Å TRYKKE PÅ NOE SOM HELST I CreateView!
    // Virker som det kan ha noe å gjøre med disse print setningene å gjøre:
     /*
    I/System.out: key: [DfnjgnyDGjRfHwGzsIDPlUJ00wI3]
    I/System.out: start status: false
    D/firebase: {DfnjgnyDGjRfHwGzsIDPlUJ00wI3={Username=lolsi@mail.no}}
    I/System.out: {DfnjgnyDGjRfHwGzsIDPlUJ00wI3={Username=lolsi@mail.no}}
    I/System.out: key: [DfnjgnyDGjRfHwGzsIDPlUJ00wI3]
      */
    // fordi run loggen fryser helt etter dette, og UI-et reagerer ikke på noen klikk


    private final Texture logo;
    private final Texture pinText;
    private final Texture waitText;
    private String gamePin;
    private GameController gameController;
    private boolean start;

    private final Stage stage;
    private final ImageButton highscoreButton;
    private final ImageButton playbookButton;
    private final ImageButton backButton;


    protected CreateView(ViewManager vm, String gamePin) {
        super(vm);
        gameController = vm.getController();
        logo = new Texture("logo.png");
        pinText = new Texture("create_pin.png");
        waitText = new Texture("create_wait.png");
        this.gamePin = gamePin;
        this.start= gameController.checkForGameStart();

        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);

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
                vm.set(new MenuView(vm));
                return true;
            }
        });
    }



    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
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
        sb.draw(pinText, (float) (FlowerPowerGame.WIDTH/2-pinText.getWidth()/2), 300);
        sb.draw(waitText, (float) (FlowerPowerGame.WIDTH/2-waitText.getWidth()/2), 200);
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.3);
        font.setColor(Color.BLACK);
        font.draw(sb, gamePin, (float) FlowerPowerGame.WIDTH/2-40, 280);
        this.start = gameController.checkForGameStart();
        if (start){
            vm.set(new PlaceBedsView(vm));
        }

        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
