package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Player;

import java.util.ArrayList;
import java.util.List;


public class HighscoreView extends View{

    private final Texture logo;
    private final Texture back;
    private final Texture highscore;
    private final Texture email;
    private final Texture score;

    protected HighscoreView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        back = new Texture("back.png");
        highscore = new Texture("Highscorelist.png");
        email = new Texture("Email.png");
        score = new Texture("Score.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth(), back.getHeight());
            if (backBounds.contains(pos.x, pos.y)) {
                vm.set(new MenuView(vm));
            }

        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    private void printHighscoreList(SpriteBatch sb){
        //TODO get list of top 10 best players, with score (DB), where nr1 in the list is the best
        List<Player> players = new ArrayList<>();
        Player pl1 = new Player();
        Player pl2 = new Player();
        pl1.update_dummyplayer("natalia@gmail.com","100");
        pl2.update_dummyplayer("sandra@gmail.com","80");
        players.add(pl2);
        players.add(pl1);

        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.25);
        font.setColor(Color.BLACK);
        float y = 255;
        for (Player player: players) {
            float username_x = FlowerPowerGame.WIDTH/2-highscore.getWidth()/2-40 ;
            float score_x = FlowerPowerGame.WIDTH/2+50;
            font.draw(sb, player.getDummyUsername(),username_x,y); //TODO update the right getters here
            font.draw(sb,player.getScore(),score_x,y);
            y-=-40;

        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,405);
        sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        sb.draw(highscore,FlowerPowerGame.WIDTH/2-highscore.getWidth()/2,355);
        sb.draw(email, FlowerPowerGame.WIDTH/2-highscore.getWidth()/2-40,315);
        sb.draw(score,FlowerPowerGame.WIDTH/2+50,315);
        printHighscoreList(sb);
        sb.end();

    }
}
