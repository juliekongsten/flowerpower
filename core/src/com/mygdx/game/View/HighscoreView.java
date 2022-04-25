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
import com.mygdx.game.Controller.ButtonController;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;
import com.mygdx.game.Model.Player;

import com.mygdx.game.Model.HighScoreList;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




public class HighscoreView extends View {

    private final Texture logo;
    private final Texture highscore;
    private final Texture email;
    private final Texture score;
    private final Stage stage;
    private final ImageButton backButton;
    private GameController gameController;
    private ButtonController buttonController;
    private HighScoreList highScoreList;
    private HashMap<String, Integer> map;


    protected HighscoreView(ViewManager vm) {
        super(vm);
        //this.highScoreList = new HighScoreList();
        this.gameController = vm.getController();
        this.buttonController = new ButtonController();
        logo = new Texture("logo.png");
        highscore = new Texture("Highscorelist.png");
        email = new Texture("Email.png");
        score = new Texture("Score.png");
        //map = gameController.getHighScore();
        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);
        backButton = buttonController.getBackButton();
        setBackButtonEvent();
        stage.addActor(backButton);
    }

   private HashMap<String, Integer> getMap(){
        map = gameController.getHighScore();
        return this.map;
    }

    private void setBackButtonEvent() {
        backButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
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

    private void printHighscoreList(SpriteBatch sb) {
        //TODO get list of top 10 best players, with score (DB), where nr1 in the list is the best


        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.25);
        font.setColor(Color.BLACK);
        float y = 300;

        if (map==null){
            HashMap<String, Integer> map = this.getMap();
        }
        Iterator hmIterator = map.entrySet().iterator();

        while(hmIterator.hasNext()){
            Map.Entry mapElement= (Map.Entry)hmIterator.next();
            float username_x = FlowerPowerGame.WIDTH / 2 - highscore.getWidth() / 2 - 30;
            float score_x = FlowerPowerGame.WIDTH / 2 + 100;
            font.draw(sb, mapElement.getKey().toString(), username_x, y); //TODO update the right getters here
            font.draw(sb, mapElement.getValue().toString(), score_x, y);
            y = y - 40;
        }

        }



    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float) 180 / 255, (float) 245 / 255, (float) 162 / 255, 1);
        sb.draw(logo, 36, 405);
        sb.draw(highscore, FlowerPowerGame.WIDTH / 2 - highscore.getWidth() / 2, 355);
        sb.draw(email, FlowerPowerGame.WIDTH / 2 - highscore.getWidth() / 2 - 30, 315);
        sb.draw(score, FlowerPowerGame.WIDTH / 2 + 70, 315);
        printHighscoreList(sb);
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        }
    }

