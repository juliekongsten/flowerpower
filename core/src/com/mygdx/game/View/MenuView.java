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
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class MenuView extends View {

    private final Texture logo;
    private final Texture join;
    private final Texture create;
    private GameController gameController;

    private final Stage stage;
    private final ImageButton playbookButton;
    private final ImageButton highscoreButton;


    protected MenuView(ViewManager vm) {
        super(vm);
        this.gameController = new GameController();
        vm.setController(gameController);
        logo = new Texture("logo.png");
        join = new Texture("join.png");
        create = new Texture("create.png");

        stage = new Stage(new FitViewport(FlowerPowerGame.WIDTH, FlowerPowerGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);


        Button playbook = new Button("playbook.png", 10, 15);
        playbookButton = playbook.getButton();
        setPlaybookButtonEvent();
        stage.addActor(playbookButton);

        Button highscore = new Button("Highscore.png", FlowerPowerGame.WIDTH - 125, 15);
        highscoreButton = highscore.getButton();
        setHighscoreButtonEvent();
        stage.addActor(highscoreButton);

        checkGame();
    }

    private void checkGame() {
        if (gameController.getGID() > 0) {
            gameController.deleteGame();
        }
    }


        private void setPlaybookButtonEvent() {
        playbookButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                //vm.set(new PlaybookView(vm));
                System.out.println("PLAYBOOK");
                return true;
            }
        });
    }

    private void setHighscoreButtonEvent() {
        highscoreButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                //Handle the input event.
                vm.set(new HighscoreView(vm));
                return true;
            }
        });
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
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
                gameController.createGame();
                String gamePin = Integer.toString(gameController.getGID());
                vm.set(new CreateView(vm, gamePin));
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
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
