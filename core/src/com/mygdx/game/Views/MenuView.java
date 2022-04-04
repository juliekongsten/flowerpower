package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class MenuView extends View {

    private Texture logo;
    private Texture playbook;
    private Texture settings;
    private Texture join;
    private Texture create;

    protected MenuView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        playbook = new Texture("playbook.png");
        settings = new Texture("settings.png");
        join = new Texture("join.png");
        create = new Texture("create.png");
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float settings_x = FlowerPowerGame.WIDTH-playbook.getWidth()-10;
            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle settingsBounds = new Rectangle(settings_x, 15, settings.getWidth(), settings.getHeight());

            if (playbookBounds.contains(pos.x, pos.y)) {
                //Hvor skal Playbook ta oss? Har tatt tilbake til StartView foreløpig
                vm.set(new StartView(vm));
                System.out.println("Playbook pressed");
            }
            if (settingsBounds.contains(pos.x, pos.y)) {
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
        float settings_x = FlowerPowerGame.WIDTH-playbook.getWidth()-10;
        sb.draw(settings, settings_x, 15);
        sb.end();
    }
}
