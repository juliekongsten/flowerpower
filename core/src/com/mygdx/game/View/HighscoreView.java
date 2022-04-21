package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class HighscoreView extends View{

    private final Texture logo;
    private final Texture back;
    private final Texture highscore;

    protected HighscoreView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        back = new Texture("back.png");
        highscore = new Texture("Highscorelist.png");
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

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,405);
        sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        sb.draw(highscore,FlowerPowerGame.WIDTH/2-highscore.getWidth()/2,345);
        sb.end();

    }
}
