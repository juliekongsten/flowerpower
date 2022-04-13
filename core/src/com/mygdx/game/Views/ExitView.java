package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
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
    private Texture exit;

    private boolean winner;


    protected ExitView(ViewManager vm) {
        super(vm);
        this.logo = new Texture("small_logo.png");
        this.gameOver = new Texture("game_over.png");
        this.replay = new Texture("replay.png");
        this.to_start = new Texture("to_start.png");
        this.winner_text = new Texture("winner_text.png");
        this.you = new Texture("you.png");
        this.fish = new Texture("fish.png");
        this.lost_text = new Texture("lost_text.png");
        this.exit = new Texture("exit.png");

        this.winner = true; //TODO: get information if the user won or not, from db
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle replayBounds = new Rectangle(FlowerPowerGame.WIDTH/2-replay.getWidth()-10,80,replay.getWidth(),replay.getHeight());
            Rectangle to_startBounds = new Rectangle(FlowerPowerGame.WIDTH/2+to_start.getWidth()/10,80,to_start.getWidth(),to_start.getHeight());
            Rectangle exitBounds = new Rectangle(FlowerPowerGame.WIDTH-exit.getWidth()-5,5, exit.getWidth(),exit.getHeight());
            if(replayBounds.contains(pos.x, pos.y)){
                vm.set(new PlaceBedsView(vm)); //assuming that replay means forwarding to new PlaceBedsView
            }
            if(to_startBounds.contains(pos.x,pos.y)){
                vm.set(new MenuView(vm));
            }
            if(exitBounds.contains(pos.x, pos.y)){
                //TODO terminate/exit the application
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
        sb.draw(logo, FlowerPowerGame.WIDTH/2-logo.getWidth()/2,FlowerPowerGame.HEIGHT-logo.getHeight()-20);
        sb.draw(gameOver,FlowerPowerGame.WIDTH/2-gameOver.getWidth()/2,400);
        sb.draw(replay,FlowerPowerGame.WIDTH/2-replay.getWidth()-10,80);
        sb.draw(to_start,FlowerPowerGame.WIDTH/2+to_start.getWidth()/10,80);
        sb.draw(exit,FlowerPowerGame.WIDTH-exit.getWidth()-5,5);

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
