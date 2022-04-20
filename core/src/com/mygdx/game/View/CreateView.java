package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;

import java.util.Random;

public class CreateView extends View {

    private final Texture logo;
    private final Texture playbook;
    private final Texture settings;
    private final Texture pinText;
    private final Texture waitText;
    private final Texture back;
    private String gamePin;
    private GameController gameController;


    protected CreateView(ViewManager vm, String gamePin) {
        super(vm);
        logo = new Texture("logo.png");
        playbook = new Texture("playbook.png");
        settings = new Texture("settings.png");
        pinText = new Texture("create_pin.png");
        waitText = new Texture("create_wait.png");
        back = new Texture("back.png");
        this.gamePin = gamePin;
    }

    private void getGamePin() {
        // Midlertidlig løsning; skal vel fikses i backend?
        // det er fikset
        /*
        Random rand = new Random();
        String result = "";
        for (int i=0; i<=6; i++) {
            result += rand.nextInt(10);
        }
        gamePin = result;
        */
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float settings_x = FlowerPowerGame.WIDTH - playbook.getWidth() - 10;
            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle settingsBounds = new Rectangle(settings_x, 15, settings.getWidth(), settings.getHeight());
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth(), back.getHeight());
            if (playbookBounds.contains(pos.x, pos.y)) {
                //vm.set(new PlaybookView(vm));
                System.out.println("Playbook pressed");
            }
            if (settingsBounds.contains(pos.x, pos.y)) {
                //vm.set(new SettingsView(vm));
                System.out.println("Settings pressed");
            }
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
        sb.draw(logo,36,375);
        sb.draw(playbook, 10, 15);
        sb.draw(pinText, (float) (FlowerPowerGame.WIDTH/2-pinText.getWidth()/2), 300);
        sb.draw(waitText, (float) (FlowerPowerGame.WIDTH/2-waitText.getWidth()/2), 200);
        float settings_x = FlowerPowerGame.WIDTH-settings.getWidth()-10;
        sb.draw(settings, settings_x, 15);
        sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        font.getData().setScale((float) 1.3);
        font.setColor(Color.BLACK);

        font.draw(sb, gamePin, (float) FlowerPowerGame.WIDTH/2-40, 280);

        sb.end();
    }
}
