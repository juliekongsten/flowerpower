package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.FlowerPowerGame;

public class JoinView extends View {
    private final Texture logo;
    private final Texture playbook;
    private final Texture highscore;
    private final Texture pinText;
    private final Texture join;
    private final Texture back;
    private final Stage stage;
    private Pixmap cursorColor;
    private TextField gamePin;
    private final float highscore_x;

    protected JoinView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        playbook = new Texture("playbook.png");
        highscore = new Texture("highscore.png");
        pinText = new Texture("join_pin.png");
        join = new Texture("join.png");
        back = new Texture("back.png");
        highscore_x = FlowerPowerGame.WIDTH-highscore.getWidth()-10;

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
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Rectangle playbookBounds = new Rectangle(10, 15, playbook.getWidth(), playbook.getHeight());
            Rectangle highscoreBounds = new Rectangle(highscore_x, 15, highscore.getWidth(), highscore.getHeight());
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth(), back.getHeight());
            Rectangle joinBounds = new Rectangle((FlowerPowerGame.WIDTH/2-join.getWidth()/2), 100, join.getWidth(), join.getHeight());

            if (joinBounds.contains(pos.x, pos.y)) {
                // Some way to check pin
                //vm.set(new GameView(vm));
                vm.set(new PlaceBedsView(vm));
                System.out.println("JOIN WAS PRESSED!");
            }
            if (playbookBounds.contains(pos.x, pos.y)) {
                //vm.set(new PlaybookView(vm));
                System.out.println("Playbook pressed");
            }
            if (highscoreBounds.contains(pos.x, pos.y)) {
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
        sb.draw(highscore, highscore_x, 15);
        sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        sb.draw(pinText, (FlowerPowerGame.WIDTH/2-pinText.getWidth()), 290);
        sb.draw(join, (FlowerPowerGame.WIDTH/2-join.getWidth()/2), 100);
        sb.end();
        stage.draw();
        stage.act();
    }
}
