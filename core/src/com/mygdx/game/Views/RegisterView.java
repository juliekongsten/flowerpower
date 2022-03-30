package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class RegisterView extends View {
    private Texture logo;
    private Texture register;
    private Stage stage;
    private TextField username;


    public RegisterView(ViewManager vm) {
        super(vm);
        logo = new Texture("logo.png");
        register = new Texture("register.png");
        stage = new Stage();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.BLACK;
        //style.cursor = style.selection = new Image(new Texture("core/assets/skin/textfield-big.png")).getDrawable();
        style.background = new Image(new Texture("inputbox.png")).getDrawable();
        username = new TextField("Username", style);
        username.setWidth(500);
        username.setHeight(37);
        username.setPosition(50, 50);
        stage.addActor(username);

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            Rectangle registerBounds = new Rectangle(100, 175, register.getWidth(), register.getHeight());
            if (registerBounds.contains(x, y)) {
                System.out.println("REGISTER PRESSED!");
                //sende inn til databasen ny bruker
                String test = username.getText();
                System.out.println(test);

                //sende videre til MenuView med innlogget bruker
            }
        }


    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)180/255,(float)245/255,(float) 162/255,1);
        sb.draw(logo,36,350);
        sb.draw(register,100,50);
        sb.end();
        stage.draw();
        stage.act();
    }
}
