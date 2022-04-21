package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class MenuView extends View {

    private final Texture logo;
    private final Texture playbook;
    private final Texture highscore;
    private final Texture join;
    private final Texture create;
    private final float highscore_x;

    protected MenuView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        playbook = new Texture("playbook.png");
        highscore = new Texture("highscore.png");
        join = new Texture("join.png");
        create = new Texture("create.png");
        highscore_x = FlowerPowerGame.WIDTH-highscore.getWidth()-10;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle highscoreBounds = new Rectangle(highscore_x, 15, highscore.getWidth(), highscore.getHeight());
            Rectangle joinBounds = new Rectangle((int) ((FlowerPowerGame.WIDTH/2)-(join.getWidth()/2)), 240,
                    join.getWidth(), join.getHeight());
            Rectangle createBounds = new Rectangle((int) ((FlowerPowerGame.WIDTH/2)-(join.getWidth()/2)), 130,
                    create.getWidth(), create.getHeight());
            if (joinBounds.contains(pos.x, pos.y)) {
                System.out.println("JOIN GAME PRESSED!");
                vm.set(new JoinView(vm));
            }
            if (createBounds.contains(pos.x, pos.y)) {
                System.out.println("CREATE GAME PRESSED!");
                vm.set(new CreateView(vm));
            }
            if (playbookBounds.contains(pos.x, pos.y)) {
                //vm.set(new PlaybookView(vm));
                System.out.println("Playbook pressed");
            }
            if (highscoreBounds.contains(pos.x, pos.y)) {
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
        sb.draw(logo,36,375);
        sb.draw(join, (float) ((FlowerPowerGame.WIDTH/2)-(join.getWidth()/2)),240);
        sb.draw(create, (float) ((FlowerPowerGame.WIDTH/2)-(join.getWidth()/2)), 130);
        sb.draw(playbook, 10, 15);
        sb.draw(highscore, highscore_x, 15);
        sb.end();
    }
}
