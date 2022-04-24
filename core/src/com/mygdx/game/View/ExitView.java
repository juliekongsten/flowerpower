package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Controller.ButtonController;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.Controller.PlayerController;
import com.mygdx.game.FlowerPowerGame;

public class ExitView extends View{

    private final Texture logo;
    private Texture gameOver;
    private Texture replay;
    private Texture to_start;
    private Texture winner_text;
    private Texture you;
    private Texture fish;
    private Texture lost_text;
    private Texture log_out;
    private PlayerController playerController;
    private GameController gameController;
    private ButtonController buttonController;
    private final ImageButton logOutButton;
    private final Stage stage;

    private boolean winner;



    protected ExitView(ViewManager vm, boolean won, GameController gameController) {
        super(vm);
        this.playerController = new PlayerController();
        this.gameController = gameController;
        this.buttonController = new ButtonController();
        this.logo = new Texture("small_logo.png");
        this.gameOver = new Texture("game_over.png");
        this.replay = new Texture("replay.png");
        this.to_start = new Texture("to_start.png");
        this.winner_text = new Texture("winner_text.png");
        this.you = new Texture("you.png");
        this.fish = new Texture("fish.png");
        this.lost_text = new Texture("lost_text.png");
        this.log_out = new Texture("log_out.png");

        this.winner = won;
        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        logOutButton = buttonController.getLogOutButton();
        setPlaybookButtonEvent();
        stage.addActor(logOutButton);
    }
    private void setPlaybookButtonEvent() {
        logOutButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                //vm.set(new PlaybookView(vm));
                playerController.logOut();
                vm.set(new StartView(vm));
                return true;
            }
        });
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle replayBounds = new Rectangle(FlowerPowerGame.WIDTH/2-replay.getWidth()-10,80,replay.getWidth(),replay.getHeight());
            Rectangle to_startBounds = new Rectangle(FlowerPowerGame.WIDTH/2+to_start.getWidth()/10,80,to_start.getWidth(),to_start.getHeight());
            Rectangle log_outBounds = new Rectangle(FlowerPowerGame.WIDTH-log_out.getWidth()-5,5, log_out.getWidth(),log_out.getHeight());
            if(replayBounds.contains(pos.x, pos.y)){
                gameController.clear();
                //TODO: Handle reset of beds?
                vm.set(new PlaceBedsView(vm)); //assuming that replay means forwarding to new PlaceBedsView
            }
            if(to_startBounds.contains(pos.x,pos.y)){
                vm.set(new MenuView(vm));
            }
            /*if(log_outBounds.contains(pos.x, pos.y)){
                //TODO log out and exit the application
                playerController.logOut();
                vm.set(new StartView(vm));
            }*/
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
        sb.draw(logo, FlowerPowerGame.WIDTH/2-logo.getWidth()/2,FlowerPowerGame.HEIGHT-logo.getHeight()-20);
        sb.draw(gameOver,FlowerPowerGame.WIDTH/2-gameOver.getWidth()/2,400);
        sb.draw(replay,FlowerPowerGame.WIDTH/2-replay.getWidth()-10,80);
        sb.draw(to_start,FlowerPowerGame.WIDTH/2+to_start.getWidth()/10,80);
        sb.draw(log_out,FlowerPowerGame.WIDTH-log_out.getWidth()-5,5);

        if(winner){
            sb.draw(winner_text,FlowerPowerGame.WIDTH/2-winner_text.getWidth()/2,350);
            sb.draw(you,FlowerPowerGame.WIDTH/2-you.getWidth()/2,230);
            sb.draw(fish, FlowerPowerGame.WIDTH/2-fish.getWidth()/2,180);
        }
        else{
            sb.draw(lost_text,FlowerPowerGame.WIDTH/2-lost_text.getWidth()/2,220);
        }
        sb.end();

    }
}
